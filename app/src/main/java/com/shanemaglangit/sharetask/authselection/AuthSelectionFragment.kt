package com.shanemaglangit.sharetask.authselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentAuthSelectionBinding
import com.shanemaglangit.sharetask.model.data.IntroItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthSelectionFragment : Fragment() {
    // Binding for the auth selection fragment
    private lateinit var binding: FragmentAuthSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Load the intro items stored under /res/values/strings.xml
        val introItems = loadIntroItems()
        // creates the adapter for the view pager
        val sliderPagerAdapter = SliderPagerAdapter(this, introItems)

        // Create the binding and inflate the layout
        binding = FragmentAuthSelectionBinding.inflate(inflater)

        // Add the adapter to the view pager
        binding.viewPagerIntro.adapter = sliderPagerAdapter

        // Attaches a dot indicator with the view pager
        TabLayoutMediator(binding.tabIntro, binding.viewPagerIntro) { tab, _ ->
            binding.viewPagerIntro.setCurrentItem(tab.position, true)
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to sign up fragment if the sign up button is clicked
        binding.buttonSignup.setOnClickListener {
            findNavController().navigate(AuthSelectionFragmentDirections.actionAuthSelectionFragmentToSignUpFragment())
        }

        // Navigate to sign in fragment if the sign in button is clicked
        binding.buttonSignin.setOnClickListener {
            findNavController().navigate(AuthSelectionFragmentDirections.actionAuthSelectionFragmentToSignInFragment())
        }
    }

    /**
     * Used to load the intro items for the view pager from the resource files
     */
    private fun loadIntroItems(): List<IntroItem> {
        // Initialize an empty list
        val items: MutableList<IntroItem> = mutableListOf()

        // Retrieves the list of images
        val imageArray = resources.getStringArray(R.array.intro_images)

        // Retrieves the list of description for the images
        val descriptionArray = resources.getStringArray(R.array.intro_desc)

        // Iterate through the retrieved data and add them to the list
        for (i in imageArray.indices) {
            items.add(
                IntroItem(
                    resources.getIdentifier(
                        imageArray[i],
                        "drawable",
                        requireActivity().packageName
                    ),
                    descriptionArray[i]
                )
            )
        }

        return items
    }

    /**
     * Inner class for the view pager adapter
     */
    private class SliderPagerAdapter(fragment: Fragment, val items: List<IntroItem>) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment =
            ItemSliderPagerFragment.newInstance(items[position])
    }
}