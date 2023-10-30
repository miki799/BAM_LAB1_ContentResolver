package com.example.lab1_contentresolver

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lab1_contentresolver.ui.theme.LAB1_ContentResolverTheme

class MainActivity : ComponentActivity() {

    object MainActivityConstants {
        const val TAG = "content_resolver"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cr = contentResolver

        setContent {
            LAB1_ContentResolverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppLayout(contentResolver = cr)
                }
            }
        }
    }
}

@SuppressLint("Range")
@Composable
fun AppLayout(contentResolver: ContentResolver, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val uri = Uri.parse("content://com.example.lab1.provider/data")
                val projection = arrayOf("uid", "name", "number")
                val selection: String? = null
                val selectionArgs: Array<String>? = null
                val sortOrder: String? = null

                Log.d(MainActivity.MainActivityConstants.TAG, "Querying ContentProvider")
                try {
                    val cursor =
                        contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

                    // iteration of the cursor to print whole table
                    if (cursor!!.moveToFirst()) {
                        val strBuild = StringBuilder()
                        while (!cursor.isAfterLast) {
                            strBuild.append(
                                """
                                    uid: ${cursor.getString(cursor.getColumnIndex("uid"))} name: ${cursor.getString(cursor.getColumnIndex("name"))} number: ${cursor.getInt(cursor.getColumnIndex("number"))}
                                    
                                """.trimIndent()
                            )
                            cursor.moveToNext()
                        }
                        Log.d(MainActivity.MainActivityConstants.TAG, "$strBuild")
                    } else {
                        Log.d(MainActivity.MainActivityConstants.TAG, "No records found")
                    }

                } catch (e: Exception) {
                    Log.d("content_resolver", "Error: ${e.message}")
                }
            }
        ) {
            Text(stringResource(R.string.button))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.text))
    }

}
