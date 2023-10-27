package com.example.showphoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.showphoto.ui.theme.ShowPhotoTheme

class MainActivity : ComponentActivity() {
    lateinit var viewModel: PhotoViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PhotoViewModel(application)
        setContent {
            Column {
                Text(text = "PHOTOS: ", fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
                ShowPhoto(viewModel = viewModel)
            }
            /*ShowPhotoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }*/
        }
    }
}

@Composable
fun ShowPhoto(viewModel: PhotoViewModel) {
    val photos = viewModel.photos.observeAsState()

    LazyColumn( contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        items(items=photos.value!!, 
            itemContent = { 
                PhotoItem(photo = it)
            })
    }
}

@Composable
fun PhotoItem(photo: Photo) {
    Row (horizontalArrangement = Arrangement.Center){
        Column {
            Spacer(modifier = Modifier.padding(vertical = 15.dp))
            Text(text = photo.title)
            AsyncImage(model = photo.url, contentDescription = "interview question...")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowPhotoTheme {
        Greeting("Android")
    }
}