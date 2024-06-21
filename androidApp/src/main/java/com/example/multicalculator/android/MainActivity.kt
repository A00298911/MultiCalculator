package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multicalculator.Greeting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingView(Greeting().greet())
                }
            }
        }
    }
}

@Composable
fun CalcView() {
    val displayText = remember { mutableStateOf("0") }
    val leftNumber = rememberSaveable { mutableStateOf(0) }
    val rightNumber = rememberSaveable { mutableStateOf(0) }
    val operation = rememberSaveable { mutableStateOf("") }
    val complete = rememberSaveable { mutableStateOf(false) }


    if (complete.value && operation.value.isNotEmpty()) {
        // a mutable variable named answer and assign a value of 0
        var answer = 0

        // Create a when statement and use the operation variable
        when (operation.value) {
            // Use the strings "+", "-", "*", and "/" to assign the outcome of the operation to the answer variable
            "+" -> answer = leftNumber.value + rightNumber.value
            "-" -> answer = leftNumber.value - rightNumber.value
            "*" -> answer = leftNumber.value * rightNumber.value
            "/" -> if (rightNumber.value != 0) answer = leftNumber.value / rightNumber.value else answer = 0
        }

        // Assign the answer variable to the displayText variable
        displayText.value = answer.toString()

        leftNumber.value = 0
        rightNumber.value = 0
        operation.value = ""
        complete.value = false
    }
    else if (operation.value.isNotEmpty() && !complete.value) {

        rightNumber.value = displayText.value.toInt()
        displayText.value = rightNumber.value.toString()
        complete.value = true
    } else {

        leftNumber.value = displayText.value.toInt()
        displayText.value = leftNumber.value.toString()
    }

    fun numberPress(btnNum: Int){
        if (complete.value) {
            leftNumber.value = 0
            rightNumber.value = 0
            operation.value = ""
            complete.value = false
        }

        if (operation.value.isNotBlank() && !complete.value) {
            rightNumber.value = rightNumber.value * 10 + btnNum
        }

        if (operation.value.isBlank() && !complete.value) {
            leftNumber.value = leftNumber.value * 10 + btnNum
        }
    }

    fun operationPress(op: String){
        if (!complete.value) {
            operation.value = op
        }
    }

    fun equalsPress(){
        complete.value = true
    }

    Column(modifier = Modifier.background(Color.LightGray)) {
        Row {
            CalcDisplay(display = displayText)
        }
        Row {
            Column{

                CalcRow(startNum = 7, numButtons = 3, onPress =  { number ->
                    numberPress(number)
                })

                Row {
                    CalcNumericButton(number = 0) { num ->
                        numberPress(num)
                    }
                    CalcEqualsButton {
                        equalsPress()
                    }
                }

            }
            Column{
                CalcOperationButton(operation = "+") { op ->
                    operationPress(op) }
                CalcOperationButton(operation = "-") { op ->
                    operationPress(op)
                }
                CalcOperationButton(operation = "*") { op ->
                    operationPress(op)
                }
                CalcOperationButton(operation = "/") { op ->
                    operationPress(op)
                }
            }
        }
    }

}

@Composable
fun CalcRow(onPress: (number: Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, onPress = onPress)
        }
    }
}

@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        text = display.value,
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp)
            .fillMaxWidth()
    )

}

@Composable
fun CalcNumericButton(number: Int, onPress: (number: Int) -> Unit){
    Button(
        onClick = { onPress(number) },

        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }

}

@Composable
fun CalcOperationButton(operation: String, onPress: (operation: String) -> Unit) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }

}

@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }

}


@Composable
fun GreetingView(text: String) {
    Text(text = text)
}


@Preview
@Composable
fun DefaultPreview_CalcView() {
    CalcView()
}

