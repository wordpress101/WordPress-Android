package org.wordpress.android.ui.stats.refresh.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import org.wordpress.android.R
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.stats.InsightsMostPopularModel
import org.wordpress.android.fluxc.store.InsightsStore
import org.wordpress.android.fluxc.store.StatsStore.InsightsTypes.MOST_POPULAR_DAY_AND_HOUR
import org.wordpress.android.modules.UI_THREAD
import org.wordpress.android.ui.stats.refresh.BlockListItem
import org.wordpress.android.ui.stats.refresh.BlockListItem.Label
import org.wordpress.android.ui.stats.refresh.BlockListItem.ListItem
import org.wordpress.android.ui.stats.refresh.BlockListItem.Title
import org.wordpress.android.ui.stats.refresh.DateUtils
import org.wordpress.android.ui.stats.refresh.InsightsItem
import org.wordpress.android.ui.stats.refresh.ListInsightItem
import org.wordpress.android.viewmodel.ResourceProvider
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

class MostPopularInsightsUseCase
@Inject constructor(
    @Named(UI_THREAD) private val mainDispatcher: CoroutineDispatcher,
    private val insightsStore: InsightsStore,
    private val dateUtils: DateUtils,
    private val resourceProvider: ResourceProvider
) : BaseInsightsUseCase(MOST_POPULAR_DAY_AND_HOUR, mainDispatcher) {
    override suspend fun loadCachedData(site: SiteModel): InsightsItem? {
        val dbModel = insightsStore.getMostPopularInsights(site)
        return dbModel?.let { loadMostPopularInsightsItem(dbModel) }
    }

    override suspend fun fetchRemoteData(site: SiteModel, forced: Boolean): InsightsItem? {
        val response = insightsStore.fetchMostPopularInsights(site, forced)
        val model = response.model
        val error = response.error

        return when {
            error != null -> createFailedItem(R.string.stats_insights_popular, error.message ?: error.type.name)
            else -> model?.let { loadMostPopularInsightsItem(model) }
        }
    }

    private fun loadMostPopularInsightsItem(model: InsightsMostPopularModel): ListInsightItem {
        val items = mutableListOf<BlockListItem>()
        items.add(Title(R.string.stats_insights_popular))
        items.add(Label(
                R.string.stats_insights_most_popular_day_and_hour_label,
                R.string.stats_insights_most_popular_views_label)
        )
        items.add(
                ListItem(
                        dateUtils.getWeekDay(model.highestDayOfWeek),
                        resourceProvider.getString(
                                R.string.stats_insights_most_popular_percent_views,
                                model.highestDayPercent.roundToInt()
                        ),
                        true
                )
        )
        items.add(
                ListItem(
                        dateUtils.getHour(model.highestHour),
                        resourceProvider.getString(
                                R.string.stats_insights_most_popular_percent_views,
                                model.highestHourPercent.roundToInt()
                        ),
                        false
                )
        )
        return createDataItem(items)
    }
}