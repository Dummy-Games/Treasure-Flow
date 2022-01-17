package typwe.cawtr.teqwa.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import typwe.cawtr.teqwa.R

@SuppressLint("CustomSplashScreen")
class fdbdgfdfd : Fragment(R.layout.bfdgffd) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.ivLogo).animate()
            .setDuration(3_000L)
            .scaleX(2f)
            .scaleY(2f)
            .withEndAction {
                findNavController().navigate(
                    R.id.startingFragment, null,
                    navOptions {
                        popUpTo(R.id.nav_graph) {
                            inclusive = true
                        }
                    }
                )
            }.start()
    }
}
