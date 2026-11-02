import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:sync_canvas/pages/splash_screen.dart';
import 'package:sync_canvas/theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final prefs = await SharedPreferences.getInstance();
  String? email = prefs.getString("email");
  String? password = prefs.getString("password");
  runApp(MyApp(email: email, password: password));
}

class MyApp extends StatelessWidget {
  final String? email;
  final String? password;
  const MyApp({super.key, required this.email, required this.password});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.system,
      title: 'Flutter Demo',
      home: SplashPage(email: email, password: password),
    );
  }
}