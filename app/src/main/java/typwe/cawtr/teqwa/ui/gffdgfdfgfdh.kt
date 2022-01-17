package typwe.cawtr.teqwa.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import typwe.cawtr.teqwa.R
import typwe.cawtr.teqwa.util.gfbgngfbngf
import typwe.cawtr.teqwa.util.gfgfdhgfhfd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.random.nextInt

class gffdgfdfgfdh : Fragment(R.layout.fbdgfdgdfgf) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fdggfdsfder(view)
    }

    private fun fdggfdsfder(view: View) {
        with(view) {
            val ivContainer = findViewById<FrameLayout>(R.id.flImages)
            viewLifecycleOwner.lifecycleScope.launch {
                listOf<ImageView>(
                    findViewById(R.id.iv1),
                    findViewById(R.id.iv2),
                    findViewById(R.id.iv3),
                    findViewById(R.id.iv4)
                ).gfgfdhgfhfd { gffdgfgf(it, ivContainer) }.forEach {
                    it.join()
                }
                Snackbar.make(view, "You won 100 points!", Snackbar.LENGTH_SHORT).show()
                fdggfdsfder(view)
            }
        }
    }

    private suspend fun gffdgfgf(iv: ImageView, container: FrameLayout): Job {
        iv.isVisible = true
        iv.setImageResource(requireContext().gfbgngfbngf("s" + Random.nextInt(1..4)))
        iv.x = container.width * Random.nextInt(20) * 0.04f
        iv.y = container.height * Random.nextInt(20) * 0.04f
        return withContext(Dispatchers.Main) { launch { fgffdfgh(iv) } }
    }

    private suspend fun fgffdfgh(iv: ImageView) = suspendCoroutine<Unit> { cont ->
        iv.setOnClickListener {
            iv.isVisible = false
            cont.resume(Unit)
        }
    }
}
