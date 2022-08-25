package com.dscvit.notix.ui.analytics


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.notix.R
import com.dscvit.notix.adapters.AnalyticsAdapter
import com.dscvit.notix.adapters.CalendarAdapter
import com.dscvit.notix.adapters.OnDateClickInterface
import com.dscvit.notix.calenderutils.CalenderUtils.selectedDate
import com.dscvit.notix.databinding.FragmentAnalyticsBinding
import com.dscvit.notix.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class AnalyticsFragment : Fragment(), OnDateClickInterface {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private var analyticsRVAdapter: AnalyticsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }


        binding.analyticsBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_analyticsFragment_to_homeFragment)
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date()).toString()
        setInsights(LocalDate.now())


        //Calendar
        selectedDate = LocalDate.now()
        setWeekView()
        binding.calenderForwardButton.setOnClickListener {
            nextWeekAction()
        }
        binding.calenderBackButton.setOnClickListener {
            previousWeekAction()
        }

        //BarChart
        //Recents Recycler View
        val analyticsRV = binding.analyticsRV
        analyticsRV.layoutManager = LinearLayoutManager(activity)
        analyticsRVAdapter = context?.let { AnalyticsAdapter(it) }
        analyticsRV.adapter = analyticsRVAdapter
        setBarChart(selectedDate, analyticsRVAdapter)

    }

    fun setBarChart(selectedDate: LocalDate?, analyticsRVAdapter: AnalyticsAdapter?) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        if (selectedDate != null) {
            val theSelectedDate: String = selectedDate.format(formatter)
            viewModel.getTodayTopApps(theSelectedDate).observe(viewLifecycleOwner) { list ->
                list?.let {
                    if (list.isNotEmpty()) {
                        binding.barChartLayoutAnalytics.visibility = View.VISIBLE
                        binding.noDataAvailableTV.visibility = View.INVISIBLE
                        if (list.size >= 4) {
                            analyticsRVAdapter?.updateList(list.subList(0, 4))
                        } else {
                            analyticsRVAdapter?.updateList(list.subList(0, list.size))
                        }

                    } else {
                        binding.barChartLayoutAnalytics.visibility = View.INVISIBLE
                        binding.noDataAvailableTV.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun setInsights(date: LocalDate?) {
        if (date != null) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val theSelectedDate: String = date.format(formatter)
            viewModel.getTotalTodayNotifs(theSelectedDate).observe(viewLifecycleOwner) { list ->
                list?.let {
                    binding.totalNotifAnalytics.text = list.size.toString()
                }
            }
            viewModel.getTotalTodaySpamNotifs(theSelectedDate).observe(viewLifecycleOwner) { list ->
                list?.let {

                    binding.totalSpamAnalytics.text = list.size.toString()
                }
            }
        }

    }


    private fun previousWeekAction() {
        selectedDate = selectedDate.minusWeeks(1)
        setWeekView()
    }

    private fun nextWeekAction() {
        selectedDate = selectedDate.plusWeeks(1)
        setWeekView()
    }

    private fun setWeekView() {

        val days: ArrayList<LocalDate?>? = daysInWeekArray(selectedDate)
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        val calendarAdapter = context?.let { CalendarAdapter(it, days, this) }
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        //Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show()
        setBarChart(date, analyticsRVAdapter)
        setInsights(date)
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun daysInWeekArray(selectedDate: LocalDate?): ArrayList<LocalDate?>? {
        val days: ArrayList<LocalDate?> = ArrayList()
        val startDate: LocalDate? = selectedDate?.let { sundayForDate(it) }
        Log.d("Start Date----------------- ", startDate.toString())
        val endDate = startDate?.plusWeeks(1)
        Log.d("End Date--------------------- ", endDate.toString())
        var current = startDate
        if (current != null) {
            while (current?.isBefore(endDate) == true) {
                days.add(current)
                current = current.plusDays(1)
            }
        }
        return days
    }

    private fun daysInWeekArray2(selectedDate: LocalDate?): ArrayList<LocalDate?>? {
        val days: ArrayList<LocalDate?> = ArrayList()
        val startDate: LocalDate? = selectedDate?.let { sundayForDate(it) }
        Log.d("Start Date----------------- ", startDate.toString())
        val endDate = startDate?.plusWeeks(1)
        Log.d("End Date--------------------- ", endDate.toString())
        var current = startDate
        if (current != null) {
            while (current?.isBefore(endDate) == true) {
                days.add(current)
                current = current.plusDays(1)
            }
        }
        return days
    }

    private fun sundayForDate(current: LocalDate): LocalDate? {
        var current = current
        val oneWeekAgo = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
            current = current.minusDays(1)
        }
        return null
    }


    ///////////////////////////////////////////////////////////
    //////    All Below this is Month View Logic
    ///////////////////////////////////////////////////////////

    private fun setMonthView() {
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        val daysInMonth: ArrayList<LocalDate?>? = daysInMonthArray(selectedDate)
        val calendarAdapter = context?.let { CalendarAdapter(it, daysInMonth, this) }
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate?): ArrayList<LocalDate?>? {
        val daysInMonthArray: ArrayList<LocalDate?>? = ArrayList()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) daysInMonthArray?.add(null) else daysInMonthArray?.add(
                LocalDate.of(selectedDate.year, selectedDate.month, i - dayOfWeek)
            )
        }
        return daysInMonthArray
    }

    fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    private fun getDay(i: Int): String {
        val remainder = i % 7
        return when (remainder) {
            1 -> "S"
            2 -> "M"
            3 -> "T"
            4 -> "W"
            5 -> "T"
            6 -> "F"
            else -> "S"
        }
    }

}

