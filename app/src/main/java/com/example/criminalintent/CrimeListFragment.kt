package com.example.criminalintent

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_crime.view.*
import kotlinx.android.synthetic.main.list_item_crime_police.*
import java.text.DateFormat
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID) }


    private var callbacks: Callbacks? = null
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var crimeRecyclerView: RecyclerView
   // private var adapter: CrimeAdapter? = null
    private var adapter: CrimeAdapter? = CrimeAdapter()
    //ch14
    private lateinit var emptyDataCrime: TextView
    private lateinit var crimeButton: Button

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
//ch14
        emptyDataCrime = view.findViewById(R.id.empty_text)
        crimeButton = view.findViewById(R.id.addCrime)

        //updateUI()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes->
                crimes.let {
                    updateUI(crimes) }
    }
        )


        crimeButton.setOnClickListener {
            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            callbacks?.onCrimeSelected(crime.id)

        }
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    // private fun updateUI() {
       // val crimes = crimeListViewModel.crimes
   private fun updateUI(crimes: List<Crime>) {
        //ch14
        if(crimes.isNotEmpty()){
        adapter = CrimeAdapter()
            emptyDataCrime.visibility = View.GONE
            crimeButton.visibility = View.GONE
            crimeRecyclerView .adapter = adapter
            val adapter= crimeRecyclerView.adapter as CrimeAdapter
            adapter.submitList(crimes)
    }else {

            crimeRecyclerView.visibility = View.GONE

        } }

    abstract open  class CrimeHolder(view: View) : RecyclerView.ViewHolder(view){
        abstract open fun bind(item:Crime)
    }

    private inner class NormalCrimeHolder(view: View) :  CrimeHolder(view),
        View.OnClickListener {
        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCrimeSelected(crime.id)
        }


        override fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()

            solvedImageView.visibility=if(crime.isSolved){
                View.VISIBLE
            }
            else
                View.GONE
        }
    }

    private  inner class DangerCrimeHolder(view: View) : CrimeHolder(view){
        private lateinit var crime: Crime
        val dangerCrimeTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dangerDateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val contactPoliceButton: Button = itemView.findViewById(R.id.contact_police_button)


        override fun bind(crime: Crime) {
            this.crime = crime
            dangerCrimeTextView.text = this.crime.title
            dangerDateTextView.text= DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
            contactPoliceButton?.setOnClickListener(View.OnClickListener { Toast.makeText(activity, "Police contacted for " + crime.title, Toast.LENGTH_SHORT).show() })

        }
    }
    inner class CrimeAdapter:
        androidx.recyclerview.widget.ListAdapter<Crime , RecyclerView.ViewHolder>(CrimeDiffUtil()) {
        val dangerCrime = 1
        val normalCrime = 2


        override fun getItemViewType(position: Int): Int {
            return if (getItem(position).isSolved== false)
                return dangerCrime
            else
                return normalCrime

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: View?
            var recyclerViewholder: RecyclerView.ViewHolder
            when (viewType) {
                dangerCrime -> {

                    val view = layoutInflater.inflate(
                        R.layout.list_item_crime_police,
                        parent,
                        false
                    )

                    recyclerViewholder = DangerCrimeHolder(view)
                }
                else -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    recyclerViewholder = NormalCrimeHolder(view)
                }

            }
            return recyclerViewholder

        }





        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val crime = getItem(position)
            if (holder is DangerCrimeHolder)
                holder.bind(crime)
            else
                if(holder is NormalCrimeHolder)
                    holder.bind(crime)
        }
    }
// challenge 11

 class CrimeDiffUtil:DiffUtil.ItemCallback<Crime>(){
     override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
         return oldItem.id === newItem.id
     }
     override fun areContentsTheSame(oldItem: Crime , newItem: Crime): Boolean {

         return oldItem ==newItem
     }

 }}
