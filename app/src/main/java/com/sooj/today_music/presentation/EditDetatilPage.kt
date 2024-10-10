package com.sooj.today_music.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicVideo
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sooj.today_music.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailPageScreen(
    navController: NavController,
    musicViewModel: MusicViewModel,
    memoViewModel: MemoViewModel
) {
    val clickedTrack by musicViewModel.selectedTrack_st.collectAsState()
    val memoEntity by musicViewModel.memoContent_st.collectAsState()

    val scrollState = rememberScrollState()

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
                    Image(
                        imageVector = Icons.Outlined.MusicVideo,
                        contentDescription = "NoteList",
                        Modifier.size(28.dp)
                    )
                }

                // update dao 로
                IconButton(onClick = {
                    navController.navigate("detail_page")

                    memoEntity?.let { memoViewModel.updateMemo_vm(memoEntity = it) }


                }) {
                    Image(
                        imageVector = Icons.Outlined.SaveAlt,
                        contentDescription = "save",
                        Modifier.size(28.dp)
                    )
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

                    clickedTrack?.image?.firstOrNull()?.url?.let { ImgUrl ->
                        AsyncImage(
                            model = ImgUrl,
                            contentDescription = "imgurl",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Text(
                        text = clickedTrack?.artist ?: "알 수 없 는 아티스트",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                    Text(
                        text = clickedTrack?.name ?: "알 수 없 는 제목",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                    Card {
                        var text by remember {
                            mutableStateOf(memoEntity?.memoContent)
                        }
                        text?.let {
                            TextField(value = text!!, onValueChange = { text = it })
                        }
                        memoEntity?.memoContent = text.toString()
                    }
                }
            }
        }
    }
}