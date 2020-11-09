//
//  UserDataSetupVC.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/5.
//

import UIKit

class UserDataSetupVC: UIViewController {
    @IBOutlet weak var imAvatar: UIImageView!
    @IBOutlet weak var tfUserPhone: UITextField!
    @IBOutlet weak var tfPassword: UITextField!
    @IBOutlet weak var btLogin: UIButton!
    @IBOutlet weak var btRegister: UIButton!
    @IBOutlet weak var btInputLogin: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the
//        self.title = "會員資料設定"
//        navigationItem.title = "會員資料設定"
        
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    
    @IBAction func clickLogin(_ sender: Any) {
        
        let userPhone = tfUserPhone.text!
        let userPwd = tfPassword.text!
        
        if userPhone.isEmpty || userPwd.isEmpty {
            return
        }
        
        let userId = login(userPhone: userPhone, userPwd: userPwd)
        if userId == 0 {
            print("Login Fail~")
        } else {
            print("Login OK!!!")
        }
        
        
        
    }
    
    
    @IBAction func clickRegister(_ sender: Any) {
    }
    
    @IBAction func ckickInputLogin(_ sender: Any) {
        tfUserPhone.text = "0900123456"
        tfPassword.text = "P@ssw0rd"
    }
    
}
