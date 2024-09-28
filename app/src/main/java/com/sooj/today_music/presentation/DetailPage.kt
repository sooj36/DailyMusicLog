package com.sooj.today_music.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sooj.today_music.R
import com.sooj.today_music.room.TrackEntity

@Composable
fun DetailPageScreen(
    navController: NavController,
    musicViewModel: MusicViewModel,
//                     memoViewModel: memoViewModel = hiltViewModel()
) {
    /** 클릭한 트랙 가져오기 */
    val clickedTrack by musicViewModel.selectedTrack
    Log.d("DetailPageScreen", "Clicked track: $clickedTrack")

    val getImageUrl by musicViewModel.getAlbumImage
    val imgUrl = remember { getImageUrl }

//    val getMemo by memoViewModel.memoContent
    Log.d("bring", "click info-> ${clickedTrack} >")
// 데이터가 제대로 전달되었는지 확인하기 위한 로그

    Log.d("DetailPageScreen", "Image URL: $imgUrl")
    val scrollState = rememberScrollState() // 스크롤 상태 기억

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Image(imageVector = Icons.Default.LibraryMusic, contentDescription = "NoteList")
                }

                IconButton(onClick = {
                    // 이미 동일한 페이지에 있을 때 다시 네비게이션 되지 않게
                    if (navController.currentDestination?.route != "edit_detail_page") {
                        navController.navigate("edit_detail_page")
                    }
                }) {
                    Image(imageVector = Icons.Default.StickyNote2, contentDescription = "edit")
                }
            }
            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imgUrl != null) {
                        Log.d("detail_image", "image_URL: ${imgUrl}")
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "image",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        Image(painterResource(id = R.drawable.img), contentDescription = "error")
                    }

                    Text(
                        text = clickedTrack?.artist ?: "알 수 없 는 아티스트",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(25.dp)
                            .padding(start = 8.dp)
                    )

                    Text(
                        text = clickedTrack?.name ?: "알 수 없 는 제목",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(21.dp)
                            .padding(start = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(20.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(20.dp),
                            text = "// [Verse 1]\n" +
                                    "How could my day be bad    \n" +
                                    "when I'm with you?         \n" +
                                    "You're the only one who makes me laugh  \n" +
                                    "So how can my day be bad?  \n" +
                                    "It's a day for you          \n" +
                                    "\n" +
                                    "// [Verse 2]\n" +
                                    "Lately, life's so boring    \n" +
                                    "I've been watching Netflix all day long  \n" +
                                    "I thought there would be    \n" +
                                    "no things left to watch     \n" +
                                    "so I let myself out         \n" +
                                    "\n" +
                                    "// [Verse 3]\n" +
                                    "When I went to the park     \n" +
                                    "I recognised you at a glance  \n" +
                                    "Face to face, we just smiled  \n" +
                                    "We already know that we'll be together  \n" +
                                    "\n" +
                                    "// [Chorus]\n" +
                                    "How could my day be bad when I'm with you?  \n" +
                                    "You're the only one who makes me laugh     \n" +
                                    "So how can my day be bad?  \n" +
                                    "It's a day for you          \n" +
                                    "\n" +
                                    "// [Verse 4]\n" +
                                    "Coffee in the morning, you and the sun  \n" +
                                    "There's a brown hue in your eyes  \n" +
                                    "How pretty it is            \n" +
                                    "I think I'm in love          \n" +
                                    "\n" +
                                    "// [Verse 5]\n" +
                                    "When I went to the park     \n" +
                                    "I recognised you at a glance  \n" +
                                    "Face to face, we smiled     \n" +
                                    "and I finally held your hands  \n" +
                                    "\n" +
                                    "// [Chorus]\n" +
                                    "How could my day be bad when I'm with you?  \n" +
                                    "You're the only one who makes me laugh  \n" +
                                    "So how can my day be bad?  \n" +
                                    "It's a day for you          \n" +
                                    "\n" +
                                    "// [Outro]\n"

                        )
                    }

                }
            } // box

        }
    }
}