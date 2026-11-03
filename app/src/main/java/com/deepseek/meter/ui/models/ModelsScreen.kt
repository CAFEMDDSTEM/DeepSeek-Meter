package com.deepseek.meter.ui.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deepseek.meter.data.model.ModelUsage
import com.deepseek.meter.ui.MainUiState
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.util.formatCount
import com.deepseek.meter.util.formatMoney
import com.deepseek.meter.util.formatToken

@Composable
fun ModelsScreen(
    state: MainUiState,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val snapshot = state.dashboard
    if (snapshot == null) {
        if (state.isLoggedIn) {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(strings[S.noModelData])
            }
        } else {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(strings[S.loginForModels], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            strings[S.modelsLoginDesc],
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Button(onClick = onLogin) { Text(strings[S.loginDeepSeek]) }
                    }
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(strings[S.modelCost], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    strings.format(S.modelsDesc, "0" to snapshot.primaryCurrency),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (snapshot.models.isEmpty()) {
            item { EmptyModelsCard() }
        } else {
            items(snapshot.models, key = { it.model }) { model ->
                ModelCard(model = model, primaryCurrency = snapshot.primaryCurrency)
            }
        }
    }
}

@Composable
private fun ModelCard(model: ModelUsage, primaryCurrency: String) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    model.model,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    formatMoney(model.costFor(primaryCurrency), primaryCurrency),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (model.costs.size > 1) {
                Text(
                    model.costs.joinToString("  ·  ") { formatMoney(it.amount, it.currency) },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModelMetric(strings[S.requests], formatCount(model.requests), Modifier.weight(1f))
                ModelMetric(strings[S.totalTokens], formatToken(model.totalTokens), Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModelMetric(strings[S.input], formatToken(model.inputTokens), Modifier.weight(1f))
                ModelMetric(strings[S.output], formatToken(model.outputTokens), Modifier.weight(1f))
            }
            ModelMetric(strings[S.cacheHit], formatToken(model.cacheHitTokens), Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun ModelMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EmptyModelsCard() {
    val strings = LocalAppStrings.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(strings[S.noModelRecords])
        }
    }
}
