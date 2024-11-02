package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class AddEditTransaksiFragment : Fragment() {
    private lateinit var viewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var supplierViewModel: SupplierViewModel

    private var barangId: Int? = 0
    private var kategoriBarang: String? = ""
    private var stokBarang: Int? = 0
    private var ukuranBarang: String? = ""
    private var jumlahBarang: Int = 0
    private var totalHargaBarang: Int? = 0
    private var hargaBarang: Int? = 0

    private var supplierId: Int? = 0
    private var supplierNama: String? = ""

    //    private var user_id: Int? = 0
//    private var username: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddEditTransaksiBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        barangId = arguments?.getInt("barangId").toString().toInt()
        if (barangId != 0) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
                binding.namaBarang.text = barang.nama_barang
                kategoriBarang = barang.kategori_barang
                stokBarang = barang.stok_barang
                ukuranBarang = barang.ukuran_barang
                binding.hargaBarang.text = barang.harga_barang.toString()
                supplierId = barang.supplier_id
                supplierNama = barang.supplier_nama
            }
        }
        binding.plus.setOnClickListener {
            if (stokBarang!! > jumlahBarang) {
                jumlahBarang += 1
            }
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }
        binding.minus.setOnClickListener {
            if (jumlahBarang > 0) {
                jumlahBarang -= 1
            }
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }
        var user_id = (requireActivity() as MainActivity).intentUserid()
        var username = (requireActivity() as MainActivity).intentUsername()
        binding.saveBtn.setOnClickListener {
            val namaBarang = binding.namaBarang.text.toString()
            val hargaBarang = binding.hargaBarang.text.toString().toInt()
            val jumlahBarang = binding.jumlahBarang.text.toString().toInt()
            val totalHarga = binding.totalHargaBarang.text.toString().toInt()
            val sisa_stok = stokBarang!! - jumlahBarang
            viewModel.insert(
                Transaksi(
                    barang_id = barangId!!,
                    barang_nama = namaBarang,
                    kategori_barang = kategoriBarang!!,
                    harga_barang = hargaBarang,
                    stok_barang = sisa_stok,
                    ukuran_barang = ukuranBarang!!,
                    jumlah_barang = jumlahBarang,
                    total_harga_barang = totalHarga,
                    user_id = user_id,
                    user_nama = username,
                    supplier_id = supplierId!!,
                    supplier_nama = supplierNama!!,
                    status = 1
                )
            )
            barangViewModel.update(
                Barang(
                    id_barang = barangId!!,
                    nama_barang = namaBarang,
                    kategori_barang = kategoriBarang!!,
                    harga_barang = hargaBarang,
                    stok_barang = sisa_stok,
                    ukuran_barang = ukuranBarang!!,
                    supplier_id = supplierId!!,
                    supplier_nama = supplierNama!!
                )
            )
            toTransaksiFragment()
        }
        return binding.root
    }

    private fun toTransaksiFragment() {
        (requireActivity() as MainActivity).toTransaksi()
    }
}
