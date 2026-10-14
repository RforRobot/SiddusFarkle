package siddu.farkle.zombie

import android.content.Context
import android.content.SharedPreferences
import android.webkit.JavascriptInterface

class WebAppInterface(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("FarkleTrilogyPrefs", Context.MODE_PRIVATE)

    @JavascriptInterface
    fun saveGameState(jsonState: String) {
        prefs.edit().putString("saved_state", jsonState).apply()
    }

    @JavascriptInterface
    fun loadGameState(): String? {
        return prefs.getString("saved_state", null)
    }
}