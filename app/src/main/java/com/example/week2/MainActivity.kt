package com.example.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Immutable
data class HardwareItem(
    val id: String,
    val name: String,
    val category: String,
    val cost: Double,
    val stockCount: Int
)

private val hardwareItems = listOf(
    HardwareItem("101", "MacBook Pro M3 Max", "Geliştirici Bilgisayarı", 3499.0, 5),
    HardwareItem("102", "NVIDIA RTX 4090 Sunucu", "Yapay Zeka / GPU", 1599.0, 2),
    HardwareItem("103", "Google Pixel 8 Pro", "Mobil Test Cihazı", 999.0, 8),
    HardwareItem("104", "Dell UltraSharp 49\"", "Monitör", 1199.0, 4),
    HardwareItem("105", "Sony WH-1000XM5 Kulaklık", "Odaklanma Ekipmanı", 399.0, 12),
    HardwareItem("106", "Raspberry Pi 5 8GB", "IoT Geliştirme Kiti", 85.0, 25),
    HardwareItem("107", "HHKB Professional Keyboard", "Ergonomik Klavye", 299.0, 0),
    HardwareItem("108", "AMD Ryzen Threadripper", "Yapay Zeka / GPU", 4999.0, 1)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DevClusterApp()
            }
        }
    }
}

@Composable
fun DevClusterApp() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var totalCost by remember { mutableDoubleStateOf(0.0) }

    val filteredItems = remember(searchQuery) {
        hardwareItems.filter { item ->
            item.name.contains(searchQuery, ignoreCase = true) ||
                item.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "DevCluster",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Donanım ve Sunucu Kaynak Yönetim Simülatörü",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        HardwareSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Toplam Harcanan Ödenek: $${String.format(Locale.US, "%.2f", totalCost)}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(
                items = filteredItems,
                key = { it.id }
            ) { item ->
                HardwareCard(
                    item = item,
                    onClick = {
                        if (item.stockCount > 0) {
                            totalCost += item.cost
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HardwareSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Donanım Ara") },
        singleLine = true
    )
}

@Composable
fun HardwareCard(
    item: HardwareItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Kategori: ${item.category}")
            Text(text = "Fiyat: $${String.format(Locale.US, "%.2f", item.cost)}")
            Text(text = "Stok: ${item.stockCount}")
            if (item.stockCount == 0) {
                Text(
                    text = "Stokta Yok",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
