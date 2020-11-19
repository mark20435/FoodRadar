//
//  AppDelegate.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/10/30.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {



    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
//        // HEX #E56C69 => UIColor 站台 https://www.uicolor.io/
//        // Navigation bar底色 #E56C69
        UINavigationBar.appearance().barTintColor = UIColor(red: 0.90, green: 0.42, blue: 0.41, alpha: 1.00)
        // Navigation <返回上一頁 文字顏色
        UINavigationBar.appearance().tintColor = UIColor.white
        // Navigation Title文字顏色 #424242
        UINavigationBar.appearance().titleTextAttributes = [NSAttributedString.Key.foregroundColor:UIColor(red: 0.26, green: 0.26, blue: 0.26, alpha: 1.00)]
        //開頭過場動畫
        Thread.sleep(forTimeInterval: 1)
        
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

