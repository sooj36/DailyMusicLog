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
import kotlinx.coroutines.launch
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


    fun getMusic(track: String) {
//        Log.i("track", track)

        viewModelScope.launch {
            try {
                val trackInfo = repository.getTrackInfo(track)
                _searchList.value = trackInfo

                // 로드된 trackInfo 객체에서 [artist] 와 [name] 값만 추출
                val nameAndArtist = trackInfo.map { method ->
                    method.name to method.artist
                }
                Log.d("겟뮤직 1 ", "로드된 값 ${_searchList.value}")
                Log.d("겟뮤직 2 (2개 메서드 추출)", "로드된 값 ${nameAndArtist}")

            } catch (e: Exception) {
                Log.e("VIEWMODEL ERROR !!", "ERROR FETCHING TRACK INFO ${e.message}")
            }
        }
        getLoadAlbumPoster()

    } // track을 기반으로 음악 정보를 검색하고, 그 결과를 viewmodel 상태로 저장

    // 선택한 트랙
    fun selectTrack(track: Track) {
        viewModelScope.launch {
            _selectedTrack.value = track
        }

        Log.d("1 내가 select한 트랙", "SELECTED TRACK : ${_selectedTrack.value}")

        getAlbumPostPoster()
    }

    // 선택한 트랙으로 앨범포스터 가져오기
    fun getAlbumPostPoster() {
        val selectedImageInfo = _selectedTrack.value ?: return


        viewModelScope.launch {
            val albumInfo = repository.getPostInfo(
                selectedImageInfo.name ?: "트랙",
                selectedImageInfo.artist ?: "아티스트"
            )
            if (albumInfo != null) {
                Log.d("1 포스터 가져오기 위한 '선택한' 앨범 정보 떴나요", "앨범은 ${albumInfo}")
                val albumImageUrl = albumInfo.image.find { it.size == "extralarge" }?.url
                _getAlbumImage.value = albumImageUrl

            } else {
                Log.e("앨범 정보 에러", "앨범 정보 못 가져옴 $$$")
            }
        }

    }

    // 로드된 트랙으로 앨범포스터 가져오기
    fun getLoadAlbumPoster() {
        val loadTrackName = _searchList.value.map { name ->
            name.name
        } ?: return

        val loadArtistName = _searchList.value.map { artist ->
            artist.artist
        }

        viewModelScope.launch {
            val albumInfo =
                repository.getPostInfo(loadTrackName.toString(), loadArtistName.toString())
            if (albumInfo != null) {
                Log.d("1 '로드된' 트랙 앨범 포스터 정보", "로드앨범포스터는 ${albumInfo}")
                val loadAlbumImageUrl = albumInfo.image.find { it.size == "extralarge" }?.url

                _getAlbumImage.value = loadAlbumImageUrl
            } else {
                Log.e("로드 앨범 에러", "앨범 정보 못 가져옴")
            }
        } //코루틴
    }

    //데이터 불러오는 메서드
    fun loadAllTracks() {
        viewModelScope.launch {
            try {
                _allTracks.value = repository.getAllTracks()
                Log.d("데이터 불러옴", "트랙s 로드 ${_allTracks.value} 성공")
            } catch (e: Exception) {
                Log.e("데이터 불러옴 오류", "트랙s 로드 ${e.message} 오류")
            }
        }
    }

    // 선택된 트랙을 데이터베이스에 저장
    fun saveSelectedTrack() {
        val trackToSave = _selectedTrack.value ?: return
        viewModelScope.launch {
            try {
                val trackEntity = TrackEntity(
                    trackName = trackToSave?.name,
                    artistName = trackToSave?.artist,
                    imageUrl = trackToSave?.image?.firstOrNull()?.url ?: "",
                )
                repository.saveToTrack(trackEntity)
                Log.d("1 선택된 트랙 db에 저장", "트랙은 ${trackEntity} 로 저장 성공")
            } catch (e : Exception) {
                Log.e("트랙 저장 오류", "트랙은 ${e.message} 로 오류 발생")
            }
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