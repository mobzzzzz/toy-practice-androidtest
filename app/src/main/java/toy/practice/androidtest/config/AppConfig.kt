package toy.practice.androidtest.config

import android.content.Context
import toy.practice.androidtest.R

object AppConfig {
    fun getDisplayName(context: Context): String {
        return context.getString(R.string.app_display_name)
    }

    fun getApiUrl(context: Context): String {
        return context.getString(R.string.api_url)
    }
}
