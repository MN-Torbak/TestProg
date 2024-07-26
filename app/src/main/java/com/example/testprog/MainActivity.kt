package com.example.testprog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testprog.ui.theme.TestProgTheme
import com.example.testprog.viewmodel.TestProgViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestProgTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel())
                }
            }
        }
    }
}

@Composable
fun MainScreen(testProgViewModel: TestProgViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        CountdownRow(testProgViewModel)

        Spacer(modifier = Modifier.size(50.dp))

        NumbersRow(testProgViewModel)

        Spacer(modifier = Modifier.size(30.dp))

        StartStopRow()
    }
}

@Composable
private fun CountdownRow(testProgViewModel: TestProgViewModel) {
    val seconds = testProgViewModel.seconds
    Row {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineSmall) {
            Text(
                text = seconds,
                fontSize = 30.sp,
            )
        }
    }
}

@Composable
private fun NumbersRow(testProgViewModel: TestProgViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButtonNumber(testProgViewModel, 1)
        TextButtonNumber(testProgViewModel, 2)
        TextButtonNumber(testProgViewModel, 3)
    }
}

@Composable
private fun TextButtonNumber(testProgViewModel: TestProgViewModel, number: Int) {
    TextButton(
        onClick = { /*testProgViewModel.clickButtonNumber(number)*/},
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {
        Text(number.toString())
    }
}

@Composable
private fun StartStopRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text("Start")
        }
        TextButton(
            onClick = { },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text("Stop")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestProgTheme {
        MainScreen()
    }
}