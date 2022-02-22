package com.nekki.shadowfi.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nekki.shadowfi.R

class fdsgfhdfgdfgsdf : Fragment(R.layout.gfdgfdhgdg) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
