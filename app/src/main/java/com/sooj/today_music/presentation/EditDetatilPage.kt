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
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailPageScreen(navController: NavController, musicViewModel: MusicViewModel, memoViewModel: MemoViewModel) {
    val clickedTrack by musicViewModel.selectedTrack
    val getImageUrl by musicViewModel.getAlbumImage
    Log.d("get track for edit", "info < ${clickedTrack} >")
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
                    Image(imageVector = Icons.Default.MusicVideo, contentDescription = "NoteList")
                }

                IconButton(onClick = {
                    navController.navigate("detail_page")
                    clickedTrack?.let { id ->
                        memoViewModel.saveMemo_vm(id.hashCode(), "")
                        Log.d("sjjjjjj", "HashCode: ${id.hashCode()}")
                    }
                }) {
                    Image(imageVector = Icons.Default.SaveAlt, contentDescription = "save")
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
                        AsyncImage(model = ImgUrl,
                            contentDescription = "imgurl",
                            modifier = Modifier.size(200.dp))
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

                    var text by remember {
                        mutableStateOf("")
                    }
                    TextField(value = text, onValueChange = { text = it })

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
                                    "so I let myself out "
                        )
                    }

                }
            }
        }
    }
}