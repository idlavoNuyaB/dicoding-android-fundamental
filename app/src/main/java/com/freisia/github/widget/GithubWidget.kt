package com.freisia.github.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.freisia.github.R
import com.freisia.github.service.StackWidgetService
import com.freisia.github.ui.detail.DetailActivity

/**
 * Implementation of App Widget functionality.
 */
class GithubWidget : AppWidgetProvider() {

    companion object{
        private const val ACTION_CLICK = "com.freisia.github.widget.ACTION_CLICK"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.github_widget)
        views.setRemoteAdapter(R.id.stack_view,intent)
        views.setEmptyView(R.id.stack_view, R.id.empty_view)

        val openIntent = Intent(context,DetailActivity::class.java)
        openIntent.action = ACTION_CLICK
        openIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        openIntent.data = openIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        val pendingIntent = PendingIntent.getActivity(context,0,openIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.stack_view,pendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

