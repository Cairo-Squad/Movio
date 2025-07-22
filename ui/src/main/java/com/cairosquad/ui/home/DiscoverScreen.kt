import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeViewModel

@Composable
fun DiscoverScreen(
    discoverContentStrategy: DiscoverContentStrategy,
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    val state by homeViewModel.screenState.collectAsState()
    Box(modifier = Modifier.background(Theme.color.surfaces.surface)) {

        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopEnd)
                .size(230.dp)
                .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(Color(0x33734EF8), shape = CircleShape)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = discoverContentStrategy.title,
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )

            CategoriesChips(
                modifier = Modifier.padding(top = 16.dp),
                categories = state.genres.map { it.name },
                selectedChipIndex = state.selectedGenreIndex,
                onChipSelected = { index ->
                    homeViewModel.onGenreSelected(index)
                })
            content()
        }
    }

}