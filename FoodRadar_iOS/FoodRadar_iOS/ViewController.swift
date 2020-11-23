//
//  ViewController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/10/30.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        // 取得使用者登入狀態並設定全域變數COMM_USER_ID的為會員ID
        if let userAccount = UserAccount.readUsersFromFile() {
            COMM_USER_ID = userAccount[0].userId
        } else {
            COMM_USER_ID = 0
        }
        
        // 關閉 Badge 功能
//        SetBadge ()
        
    }
    
    func SetBadge () {
        // 設定TabBar會員專區顯示的badge等於會員ID
        if let tabItems = tabBarController?.tabBar.items {
            let tabItem = tabItems[4] // TabBar會員專區
            if COMM_USER_ID == 0 {
                tabItem.badgeValue = nil
                print("SetBadge.COMM_USER_ID == 0")
            } else {
                tabItem.badgeValue = String(COMM_USER_ID)
                print("SetBadge.String(COMM_USER_ID)")
            }
        }
    }


}

