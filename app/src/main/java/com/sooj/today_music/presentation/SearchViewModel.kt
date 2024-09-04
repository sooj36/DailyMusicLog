package com.sooj.today_music.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sooj.today_music.domain.SearchRepository
import com.sooj.today_music.domain.Track
import com.sooj.today_music.room.TrackEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** _searchList.value <-- _searchList 값 가져와 외부에 노출
 * val searchList: State<List<Track>> get() = _searchList
get() 커스텀 게터 */

/** private val _infoList = mutableStateOf<List<Album>>(emptyList())
val infoList: State<List<Album>> get() = _infoList */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
    /** viewmodel 생성 시 Hilt가 알아서 repo 제공해주고, 이 주입받은 repo통해 데이터 처리*/
) : ViewModel() {

    // 검색
    private val _searchList = mutableStateOf<List<Track>>(emptyList()) // 여러개의 객체 담고 있어서 List
    val searchList: State<List<Track>> get() = _searchList

    // 선택
    private val _selectedTrack = mutableStateOf<Track?>(null)
    val selectedTrack: State<Track?> get() = _selectedTrack

    // 선택 트랙에서 Artist, Track명으로 get.Info 가져오기
    private val _getAlbumImage = mutableStateOf<String?>(null)
    val getAlbumImage: State<String?> get() = _getAlbumImage

    // 모든 트랙 데이터 상태 관리
    private val _allTracks = mutableStateOf<List<TrackEntity>>(emptyList())
    val allTracks : State<List<TrackEntity>> get() = _allTracks

    /** track을 기반으로 음악 정보를 검색하고, 그 결과를 viewmodel 상태로 저장 */
    fun getMusic(track: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("sj_vm(↓) GETMUSIC", "Running on thread: ${Thread.currentThread().name}")
            try {
                val trackInfo = repository.getMusic_impl(track)
                withContext(Dispatchers.Main) {
                    _searchList.value = trackInfo
                }

//                // 로드된 trackInfo 객체에서 [artist] 와 [name] 값만 추출
//                val nameAndArtist = trackInfo.map { method ->
//                    method.name to method.artist
//                }
//                withContext(Dispatchers.Main) {
//                    _searchList.value = trackInfo
//                }

            } catch (e: Exception) {
                Log.e("sj VIEWMODEL ERROR !!", "ERROR FETCHING TRACK INFO ${e.message}")
            }
            Log.d("sj_vm(↑) GETMUSIC", "Running on thread: ${Thread.currentThread().name}")
        }
        // getLoadAlbumPoster()
    }

    // 선택한 트랙
    fun selectTrack(track: Track) {
            /** track 선택 시 즉시 상태 업데이트*/
            _selectedTrack.value = track

        Log.d("sj VM SELECT", "SELECTED TRACK : ${_selectedTrack.value}")

        getAlbumPoster()
    }

    // 선택한 트랙으로 앨범포스터 가져오기
    fun getAlbumPoster() {
        val selectedImageInfo = _selectedTrack.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("sj_vm(↓) GETPOSTER", "Running on thread: ${Thread.currentThread().name}")

            val albumInfo = repository.getAlbumPoster_impl(
                selectedImageInfo.name ?: "트랙",
                selectedImageInfo.artist ?: "아티스트"
            )
            if (albumInfo != null) {
                Log.d("1 포스터 가져오기 위한 '선택한' 앨범 정보 떴나요", "앨범은 ${albumInfo}")
                val albumImageUrl = albumInfo.image.find { it.size == "extralarge" }?.url
                withContext(Dispatchers.Main) {
                    _getAlbumImage.value = albumImageUrl
                    Log.d("sj_vm getposter withcontext", "Running on thread: ${Thread.currentThread().name}")
                }
            } else {
                Log.e("앨범 정보 에러", "앨범 정보 못 가져옴 $$$")
            }
            Log.d("sj_vm(↑) GETPOSTER", "Running on thread: ${Thread.currentThread().name}")
        }

    }

    // 로드된 트랙으로 앨범포스터 가져오기
  /**  fun getLoadAlbumPoster() {
        val loadTrackName = _searchList.value.map { name -> name.name } ?: return

        val loadArtistName = _searchList.value.map { artist -> artist.artist }

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("sj_VM 앞 포스터", "Running on thread: ${Thread.currentThread().name}")
            val albumInfo = repository.getPostInfo(loadTrackName.joinToString(",") , loadArtistName.joinToString(","))
            if (albumInfo != null) {
                Log.d("1 '로드된' 트랙 앨범 포스터 정보", "로드앨범포스터는 ${albumInfo}")
                val loadAlbumImageUrl = albumInfo.image.find { it.size == "extralarge" }?.url
                _getAlbumImage.value = loadAlbumImageUrl
            } else {
                Log.e("로드 앨범 에러", "앨범 정보 못 가져옴")
            }
            Log.d("sj_VM 뒤 포스터", "Running on thread: ${Thread.currentThread().name}")
        } //코루틴
    } */

    //데이터 불러오는 메서드
    fun getAllTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allTracks.value = repository.getAllTracks_impl()
                Log.d("데이터 불러옴", "트랙s 로드 ${_allTracks.value} 성공")
            } catch (e: Exception) {
                Log.e("데이터 불러옴 오류", "트랙s 로드 ${e.message} 오류")
            }
            Log.d("sj vm GETALL", "Running on thread: ${Thread.currentThread().name}")
        }
    }

    // 선택된 트랙을 데이터베이스에 저장
    fun saveSelectedTrack() {
        val trackToSave = _selectedTrack.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("sj_VM(↓) SAVE", "Running on thread: ${Thread.currentThread().name}")
            try {
                val trackEntity = TrackEntity(
                    trackName = trackToSave?.name,
                    artistName = trackToSave?.artist,
                    imageUrl = trackToSave?.image?.firstOrNull()?.url ?: "",
                )
                repository.saveSelectedTrack_impl(trackEntity)
                Log.d("1 트랙 db에 저장", "트랙은 ${trackEntity} 로 저장 성공")
            } catch (e : Exception) {
                Log.e("트랙 저장 오류", "트랙은 ${e.message} 로 오류 발생")
            }
            Log.d("sj_VM(↑) SAVE", "Running on thread: ${Thread.currentThread().name}")
        }
    }


    //

// coroutines( data 의존성 있는 경우) //
//        viewModelScope.launch {
//            val response = musicApi.getMusicSearch(
//                "track.search",
//                track,
//                BuildConfig.LAST_FM_API_KEY,
//                "json"
//            )
//
//            if (response.isSuccessful) {
//                // 응답 성공 시,
//                val musicModel = response.body()
//                Log.d("API RESPONSE,", musicModel.toString())
//
//                response.body()?.results?.trackmatches?.track?.let { tracks ->
//                    _searchList.value = tracks
//                    /** searchlist에 값을 할당*/
//
//                    tracks.forEach { track ->
//                        Log.d(
//                            "API RESPONSE foreach",
//                            "Track: ${track.name}, Artist: ${track.artist}"
//                        )
//                    } // track
//                }
//            } else {
//                println("response error")
//                Log.e(
//                    "response error",
//                    "Error Code 에러 번호는 ! : ${response.code()}, message:${response.message()}"
//                )
//            }
//        } //viewModelScope
}