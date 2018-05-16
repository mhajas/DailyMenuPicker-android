package soft.brunhilda.org.dailymenupicker

import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import soft.brunhilda.org.dailymenupicker.fragments.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displaySelectedScreen(R.id.nav_restaurants) // First start TODO

        Hawk.init(this).build() // no idea where to put this


    }

    fun displaySelectedScreen(id: Int){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when (id) {
            R.id.nav_restaurants -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, TodayAllRestaurantFragment.getInstance()).commit()
            }
            R.id.nav_today_all_foods -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, TodayAllFoodFragment.getInstance()).commit()
            }
            R.id.nav_favorite_ingredients -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, FavoriteIngredientsFragment.getInstance()).commit()
            }
            R.id.nav_favorite_restaurants -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, FavouriteRestaurantsFragment.getInstance()).commit()
            }
            R.id.nav_agenda -> {
                fragmentManager.beginTransaction().replace(R.id.content_main, AgendaFragment.getInstance()).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Settings will be here",Toast.LENGTH_LONG).show()
                return true
        }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.itemId)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
