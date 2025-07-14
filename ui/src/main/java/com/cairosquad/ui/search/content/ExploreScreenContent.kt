package com.cairosquad.ui.search.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.MovieCard
import com.cairosquad.design_system.component.SectionHeader
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun ExploreScreenContent(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    listener: SearchInteractionListener
) {

    val lazyColumnState = rememberLazyListState()
    val scrollOffset by remember {
        derivedStateOf {
            lazyColumnState.firstVisibleItemScrollOffset.toFloat()
        }
    }
    val shadowAlpha by animateFloatAsState(
        targetValue = if (scrollOffset > 0) 0.06f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyColumnState,
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        stickyHeader {
            InputField(
                modifier = Modifier
                    .dropShadow(
                        shape = RectangleShape,
                        color = Theme.color.system.dropShadow,
                        offsetX = 0.dp,
                        offsetY = 1.dp,
                        blur = 12.dp,
                        spread = 0.dp,
                        alpha = shadowAlpha
                    )
                    .background(Theme.color.surfaces.surface)
                    .padding(16.dp),
                value = state.query,
                onValueChange = listener::onQueryTextChanged,
                placeholder = stringResource(R.string.search),
                leadingIcon = R.drawable.search_bottom_nav,
                onFocusChanged = {
                    if (it) {
                        listener.onClickSearchTextField()
                    }
                },
                readOnly = true
            )
        }
        item {
            SectionHeader(
                title = stringResource(R.string.for_you),
                actionText = stringResource(R.string.see_all),
                actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                onActionClick = {}
            )
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.forYou) { movie ->
                    MovieCard(
                        modifier = Modifier.width(124.dp),
                        title = movie.title,
                        vote = movie.rating,
                        imgUrl = movie.posterPath,
                        width = 124.dp,
                        aspectRatio = 0.775f
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            SectionHeader(
                title = stringResource(R.string.explore_more),
            )

        }
        item {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(max = (state.exploreMore.size * 2 * 240).dp),
                columns = GridCells.Adaptive(minSize = 158.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(state.exploreMore) { movie ->

                    MovieCard(
                        title = movie.title,
                        vote = movie.rating,
                        imgUrl = movie.posterPath,
                        width = null,
                        aspectRatio = 0.877f
                    )
                }
            }
        }
    }
}
