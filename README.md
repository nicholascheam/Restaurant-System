java project
User clicks login button
↓
LoginController.handleLogin()
↓
AuthService.login()
↓
User object returned
↓
FXMLLoader loads Menu.fxml
↓
MenuController created
↓
setUser(user) ← YOU pass data
↓
Scene switches