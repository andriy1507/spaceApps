package com.spaceapps.myapplication.features.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.spaceapps.myapplication.features.dayGradient
import com.spaceapps.myapplication.models.PostEntity
import com.spaceapps.myapplication.ui.MyApplicationTheme
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import org.joda.time.LocalDateTime

const val POST_ITEM_HEIGHT = 150

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostsScreen() {
    val vm = viewModel(PostsViewModel::class.java)
    val lazyListState = rememberLazyListState()
    val events = vm.events.collectAsState(initial = InitState)
    val posts = vm.posts.observeAsState(emptyList())
    val query = vm.query.observeAsState()
    val searchResult = vm.searchResults.observeAsState(emptyList())
    val scaffoldState = rememberScaffoldState()
    val paging = vm.postsFlow.collectAsLazyPagingItems()
    MyApplicationTheme {
        Scaffold(scaffoldState = scaffoldState) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Brush.linearGradient(dayGradient))
            ) {
//                items(paging) { post ->
//                    if (post != null) {
//                        PostItemView(post = post)
//                    } else {
//                        CircularProgressIndicator()
//                    }
//                }
//                itemsIndexed(posts.value) { index, item ->
//                    PostItemView(post = item)
//                    if (index == posts.value.lastIndex && index + 1 == vm.itemsLoaded) {
//                        vm.fetchData()
//                    }
//                }
//                if (events.value == PostsLoading && vm.canLoad) {
//                    item {
//                        CircularProgressIndicator()
//                    }
//                }
            }
        }
    }
}

@Composable
fun PostItemView(post: PostEntity) {
    Text(
        text = post.toString(),
        modifier = Modifier
            .fillMaxWidth()
            .height(POST_ITEM_HEIGHT.dp)
    )
}

@Preview
@Composable
fun Preview() {
    MyApplicationTheme {
        PostItemView(
            post = PostEntity(
                id = 0,
                title = "Title",
                text = LoremIpsum(15).toString(),
                created = LocalDateTime(),
                isLiked = false,
                likesCount = 0,
                commentsCount = 0
            )
        )
    }
}
