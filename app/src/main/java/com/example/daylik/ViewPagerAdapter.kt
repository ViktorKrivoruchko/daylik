package com.example.daylik

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2 // Два фрагмента

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment() // Левое окно (временные задачи)
            1 -> SecondFragment() // Правое окно (постоянные задачи)
            else -> throw IllegalStateException("Invalid position")
        }
    }
}