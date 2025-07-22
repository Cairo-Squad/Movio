import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.home.composable.DiscoverMediaItems
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpComingScreen(modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.screenState.collectAsState()
    val strategy=UpComingStrategy(mediaType = MediaType.Movies)
    val navController= LocalNavController.current
    DiscoverScreen(
        discoverContentStrategy = strategy,
        navController = navController,
        homeViewModel = homeViewModel
    ) {
        DiscoverMediaItems(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            discoverContentStrategy = strategy,
            state = state,
            listener = homeViewModel
        )

    }
}