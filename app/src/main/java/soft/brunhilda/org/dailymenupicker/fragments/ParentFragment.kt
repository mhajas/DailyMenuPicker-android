package soft.brunhilda.org.dailymenupicker.fragments

import android.app.Fragment
import soft.brunhilda.org.dailymenupicker.R

abstract class ParentFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        activity.setTitle(R.string.app_name)
    }
}