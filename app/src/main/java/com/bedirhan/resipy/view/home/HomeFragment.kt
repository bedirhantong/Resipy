package com.bedirhan.resipy.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bedirhan.resipy.adapter.ResipyRecyclerAdapter
import com.bedirhan.resipy.databinding.FragmentHomeBinding
import java.util.UUID


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private val resipyRecyclerAdapter : ResipyRecyclerAdapter by lazy {
        ResipyRecyclerAdapter(
            :: onClickRecyclerRow
        )
    }


    private fun onClickRecyclerRow(uuid: Int){
        val action = HomeFragmentDirections.actionHomeFragmentToResipyDetail(uuid)
        Navigation.findNavController(requireView()).navigate(action)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.refreshData()


        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.homeRecyclerView.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getDataFromInternetDirectly()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.homeRecyclerView.adapter = resipyRecyclerAdapter
        observeLiveData()

    }


    // burada viewmodelda değişen livedataları observe edeceğiz
    private fun observeLiveData(){
        viewModel.resipies.observe(viewLifecycleOwner){
            // burada adapter a bağlayacağız verileri
            resipyRecyclerAdapter.submitList(it)

            // recyclerview görünüyor mu ona bakmamız lazım
            binding.homeRecyclerView.visibility = View.VISIBLE
//            binding.errorMessage.visibility = View.GONE
        }


        viewModel.errorMessage.observe(viewLifecycleOwner){
            if (it){
                binding.errorMessage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }else{
                binding.errorMessage.visibility = View.GONE
            }
        }

        viewModel.isLoadingResipy.observe(viewLifecycleOwner){
            if (it){
                binding.errorMessage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}