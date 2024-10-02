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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sooj.today_music.room.MemoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPageScreen(
    navController: NavController,
    musicViewModel: MusicViewModel = hiltViewModel(),
    memoViewModel: MemoViewModel = hiltViewModel(),
    ) {

    // test
    val trackClick by musicViewModel.selectedTrackEntity.collectAsState()

    // 선택된 트랙의 trackid로 memoentity 불러오기
    LaunchedEffect(trackClick) {
        trackClick?.let { te ->
            musicViewModel.loadMemoForTrack_test(te.trackId)
        }
    }

    //memoentity감지
    val memoEntity by musicViewModel.memoContent.collectAsState()

    /** 클릭한 트랙 가져오기 */
    val clickedTrack by musicViewModel.selectedTrack
    Log.d("DetailPageScreen", "Clicked track: $clickedTrack")

    val getImageUrl by musicViewModel.getAlbumImage
    val imgUrl = remember { getImageUrl }

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
//                    // 이미 동일한 페이지에 있을 때 다시 네비게이션 되지 않게
//                    if (navController.currentDestination?.route != "edit_detail_page") {
                    navController.navigate("edit_detail_page")
//                    }

                }) {
                    Image(imageVector = Icons.Default.StickyNote2, contentDescription = "edit")
                }
            }
            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imgUrl != null) {
//                        Log.d("detail_image", "image_URL: ${imgUrl}")
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "image",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
//                        Image(painterResource(id = R.drawable.img), contentDescription = "error")
                    }
                    //Coil 사용
                    clickedTrack?.image?.firstOrNull()?.url?.let { DbUrl ->
                        AsyncImage(
                            model = DbUrl, contentDescription = "img",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(top = 8.dp)
                        )

                    }

                    Text(
                        text = clickedTrack?.artist ?: "알 수 없 는 아티스트",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 5.dp),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )

                    Text(
                        text = clickedTrack?.name ?: "알 수 없 는 제목",
                        modifier = Modifier
                            .padding(top = 3.dp),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp
                    )
                    Card(
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(20.dp),
                    ) {
                        Column(Modifier.fillMaxWidth()
                            .padding(15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            memoEntity ?.let { mmm ->
                                Text(text = "${mmm?.memoContent} \n ${mmm.trackId}",textAlign = TextAlign.Center,)
                            } ?: Text(text = "no load trackid",textAlign = TextAlign.Center,)
                        }
                    } // c
                } // box
            } // c
        }
    }
}