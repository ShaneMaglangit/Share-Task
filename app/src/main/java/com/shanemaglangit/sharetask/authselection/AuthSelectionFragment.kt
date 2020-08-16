package com.shanemaglangit.sharetask.authselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.FragmentAuthSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthSelectionFragment : Fragment() {
    private lateinit var binding: FragmentAuthSelectionBinding

    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val introItems = loadIntroItems()
        val sliderPagerAdapter = SliderPagerAdapter(this, introItems)

        binding = FragmentAuthSelectionBinding.inflate(inflater)
        binding.viewPagerIntro.adapter = sliderPagerAdapter

        TabLayoutMediator(binding.tabIntro, binding.viewPagerIntro) {tab, _ ->
            binding.viewPagerIntro.setCurrentItem(tab.position, true)
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignup.setOnClickListener {
            findNavController().navigate(R.id.action_authSelectionFragment_to_signUpFragment)
        }

        binding.buttonSignin.setOnClickListener {
            findNavController().navigate(R.id.action_authSelectionFragment_to_signInFragment)
        }

    }

    private fun loadIntroItems(): List<IntroItem> {
        val items: MutableList<IntroItem> = mutableListOf()
        val imageArray = resources.getStringArray(R.array.intro_images)
        val descriptionArray = resources.getStringArray(R.array.intro_desc)

        for (i in imageArray.indices) {
            items.add(
                IntroItem(
                    resources.getIdentifier(imageArray[i], "drawable", requireActivity().packageName),
                    descriptionArray[i]
                )
            )
        }

        return items
    }

    private inner class SliderPagerAdapter(fragment: Fragment, val items: List<IntroItem>) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment =
            ItemSliderPagerFragment.newInstance(items[position])
    }
}