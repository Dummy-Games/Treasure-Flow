package typwe.cawtr.teqwa.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import typwe.cawtr.teqwa.R

class fgfggfdggfd : Fragment(R.layout.fdggfgfgfdfbgdbg) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnAbout).setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }

        view.findViewById<Button>(R.id.btnPlay).setOnClickListener {
            findNavController().navigate(R.id.gameFragment)
        }
    }
}
