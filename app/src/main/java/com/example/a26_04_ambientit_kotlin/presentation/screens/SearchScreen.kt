package com.example.a26_04_ambientit_kotlin.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a26_04_ambientit_kotlin.presentation.ui.theme.A26_04_ambientit_kotlinTheme
import com.example.a26_04_ambientit_kotlin.presentation.viewmodel.MainViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true,
           uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    A26_04_ambientit_kotlinTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SearchScreen(modifier = Modifier.padding(innerPadding).background(Color.Red))
        }
    }
}

@Composable
fun SearchScreen(
    modifier:Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()) {

    Column(modifier= modifier
        .background(Color.Yellow)
        .padding(8.dp)
        .background(Color.Green)
        .fillMaxSize()

    ) {

        println("SearchScreen()")
        Text(text = "Text1",fontSize = 20.sp)
        Spacer(Modifier.size(8.dp))
        Text(text = "Text2",
            fontSize = 14.sp,
            modifier= Modifier.background(Color.Red))

        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value

        repeat(list.size) {
            PictureRowItem(list[it].name, Color.Blue)
        }
    }
}

@Composable
fun PictureRowItem(text: String, color: Color){
    Text(text = text,fontSize = 20.sp, color = color)
}