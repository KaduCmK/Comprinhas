package io.github.kaducmk.comprinhas.auth.presentation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.kaducmk.comprinhas.R
import io.github.kaducmk.comprinhas.auth.data.AuthService
import io.github.kaducmk.comprinhas.auth.data.model.AuthUiEvent
import io.github.kaducmk.comprinhas.auth.data.model.AuthUiState
import io.github.kaducmk.comprinhas.ui.navigation.ToHome
import io.github.kaducmk.comprinhas.ui.navigation.ToHomeNavGraph
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun AuthScreenRoot(
    modifier: Modifier = Modifier,
    authService: AuthService,
    navController: NavController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    AuthScreen(
        modifier = modifier,
        authService = authService,
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        uiEvent = viewModel::onEvent
    )
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authService: AuthService?,
    navController: NavController,
    uiState: AuthUiState,
    uiEvent: (AuthUiEvent) -> Unit
) {

    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bem-Vindo!", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { uiEvent(AuthUiEvent.OnLogin(authService!!)) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.google_icon_logo_svgrepo_com),
                        contentDescription = "Lista Existente"
                    )
                    Text(
                        text = "Entrar com Google",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            when (uiState) {
                is AuthUiState.Loading -> Text("Carregando...")
                is AuthUiState.Unauthenticated -> {
                    uiState.error?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is AuthUiState.Authenticated -> {
                    Toast.makeText(LocalContext.current, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show()
                    navController.navigate(ToHomeNavGraph)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview() {
    ComprinhasTheme {
        AuthScreen(
            authService = null,
            navController = rememberNavController(),
            uiState = AuthUiState.Unauthenticated(null),
            uiEvent = {}
        )
    }
}