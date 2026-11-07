package com.deepseek.meter.ui.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deepseek.meter.data.model.DeepSeekServiceStatus
import com.deepseek.meter.data.model.ServiceHealth
import com.deepseek.meter.data.model.StatusComponent
import com.deepseek.meter.data.model.StatusIncident
import com.deepseek.meter.ui.MainUiState
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.util.formatTime
import java.time.Instant
import java.util.Locale

@Composable
fun StatusScreen(
    state: MainUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val status = state.serviceStatus
    if (status == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.statusLoading) CircularProgressIndicator() else Text(state.statusError ?: strings[S.statusUnavailable])
                if (!state.statusLoading) Button(onClick = onRefresh) { Text(strings[S.retry]) }
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
                Text(strings[S.statusPage], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(strings[S.statusDescription], style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { OverallStatusCard(status, onRefresh, state.statusLoading) }
        if (status.sections.isNotEmpty() || status.ungroupedComponents.isNotEmpty()) {
            item { SectionTitle(strings[S.components]) }
            status.ungroupedComponents.forEach { component ->
                item(key = "component-${component.id}") { ComponentCard(component) }
            }
            status.sections.forEach { section ->
                item(key = "section-${section.id}") { SectionTitle(section.name) }
                items(section.components, key = { it.id }) { component -> ComponentCard(component) }
            }
        }
        item { SectionTitle(strings[S.activeIncidents]) }
        if (status.activeIncidents.isEmpty()) {
            item { EmptyState(strings[S.noActiveIncidents]) }
        } else {
            items(status.activeIncidents, key = { it.id }) { incident -> IncidentCard(incident) }
        }
        item { SectionTitle(strings[S.recentIncidents]) }
        if (status.recentIncidents.isEmpty()) {
            item { EmptyState(strings[S.noRecentIncidents]) }
        } else {
            items(status.recentIncidents, key = { it.id }) { incident -> IncidentCard(incident, compact = true) }
        }
    }
}

@Composable
private fun OverallStatusCard(status: DeepSeekServiceStatus, onRefresh: () -> Unit, loading: Boolean) {
    val strings = LocalAppStrings.current
    val color = healthColor(status.overall)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(12.dp).background(color, CircleShape))
                Text(healthLabel(status.overall), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            }
            Text(
                strings.format(S.lastChecked, "0" to formatTime(status.lastUpdated)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onRefresh, enabled = !loading) {
                Text(if (loading) strings[S.refreshing] else strings[S.refresh])
            }
        }
    }
}

@Composable
private fun ComponentCard(component: StatusComponent) {
    val strings = LocalAppStrings.current
    val color = healthColor(component.health)
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(Modifier.size(9.dp).background(color, CircleShape))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(component.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                component.uptimePercent?.let {
                    Text(
                        "${strings[S.uptime]}: ${String.format(Locale.ROOT, "%.2f", it)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(healthLabel(component.health), style = MaterialTheme.typography.labelMedium, color = color)
        }
    }
}

@Composable
private fun IncidentCard(incident: StatusIncident, compact: Boolean = false) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(incident.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(incident.status.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            if (!compact && incident.description.isNotBlank()) {
                Text(incident.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (incident.affectedComponents.isNotEmpty()) {
                Text(
                    incident.affectedComponents.joinToString(" · "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun EmptyState(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun healthLabel(health: ServiceHealth): String {
    val strings = LocalAppStrings.current
    return when (health) {
        ServiceHealth.Operational -> strings[S.allOperational]
        ServiceHealth.Degraded -> strings[S.degraded]
        ServiceHealth.PartialOutage -> strings[S.partialOutage]
        ServiceHealth.FullOutage -> strings[S.fullOutage]
        ServiceHealth.Maintenance -> strings[S.maintenance]
        ServiceHealth.Unknown -> strings[S.statusUnknown]
    }
}

private fun healthColor(health: ServiceHealth): Color = when (health) {
    ServiceHealth.Operational -> Color(0xFF15803D)
    ServiceHealth.Degraded, ServiceHealth.Maintenance -> Color(0xFFB45309)
    ServiceHealth.PartialOutage -> Color(0xFFC2410C)
    ServiceHealth.FullOutage -> Color(0xFFB91C1C)
    ServiceHealth.Unknown -> Color(0xFF6B7280)
}
