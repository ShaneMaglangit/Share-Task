package com.shanemaglangit.sharetask.authselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shanemaglangit.sharetask.databinding.FragmentItemSliderPagerBinding
import com.shanemaglangit.sharetask.model.data.IntroItem


class ItemSliderPagerFragment : Fragment() {

    companion object {
        fun newInstance(item: IntroItem): ItemSliderPagerFragment {
            val args = Bundle()
            args.putInt("imageId", item.imageId)
            args.putString("description", item.description)

            val fragment = ItemSliderPagerFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemSliderPagerBinding.inflate(inflater)
        val args = requireArguments()

        binding.imageIllustration.setImageResource(args.getInt("imageId"))
        binding.textDetails.text = args.getString("description")

        return binding.root
    }
}