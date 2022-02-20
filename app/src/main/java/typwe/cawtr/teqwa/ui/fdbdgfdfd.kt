package typwe.cawtr.teqwa.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.collect
import typwe.cawtr.teqwa.R

private class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SomeViewModel(context) as T
    }
}

@SuppressLint("CustomSplashScreen")
class fdbdgfdfd : Fragment(R.layout.bfdgffd) {

    private val viewModel: SomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.result.collect {
                if (it.first) {
                    start(it.second!!)
                } else {
                    findNavController().navigate(
                        R.id.startingFragment, null,
                        navOptions {
                            popUpTo(R.id.nav_graph) {
                                inclusive = true
                            }
                        }
                    )
                }
            }
        }
    }

    private fun start(url: String) {
        // starting web activity
        startActivity(
            Intent(requireContext(), WebViewActivity::class.java)
                .putExtra(
                    "url",
                    url
                )
        )

        requireActivity().finish()
    }
}
