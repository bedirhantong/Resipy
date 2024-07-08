package com.bedirhan.resipy.view.detailPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bedirhan.resipy.databinding.FragmentResipyDetailBinding
import com.bedirhan.resipy.util.customizedPlaceHolder
import com.bedirhan.resipy.util.downloadImage


class ResipyDetail : Fragment() {
    private var _binding: FragmentResipyDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var resipyId = 0
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResipyDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        arguments?.let {
            resipyId = ResipyDetailArgs.fromBundle(it).recipyId
        }
        viewModel.getResipyFromLocalDb(resipyId)
        observeLiveData()


    }

    fun observeLiveData(){
        viewModel.resipyData.observe(viewLifecycleOwner) { resipy ->
            binding.recipyNameText.text = resipy.isim
            binding.recipyCaloryDetail.text = resipy.kalori
            binding.reciptCarbonhydratDetail.text = resipy.karbonhidrat
            binding.recipyProteinDetail.text = resipy.protein
            binding.recipyFatDetail.text = resipy.yag
            binding.imageViewDetail.downloadImage(resipy.gorsel, customizedPlaceHolder(requireContext()))
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}