package typwe.cawtr.teqwa.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import typwe.cawtr.teqwa.R

class fdsgfhdfgdfgsdf : Fragment(R.layout.gfdgfdhgdg) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
