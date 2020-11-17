//
//  UserDataSetupVC.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/5.
//

import UIKit

class UserDataSetupVC: UIViewController {
    @IBOutlet weak var imAvatar: UIImageView!
    
    @IBOutlet weak var labPhone: UILabel!
    @IBOutlet weak var tfUserPhone: UITextField!
    
    @IBOutlet weak var labPassword: UILabel!
    @IBOutlet weak var tfPassword: UITextField!
    
    @IBOutlet weak var btLogin: UIButton!
    @IBOutlet weak var btRegister: UIButton!
    @IBOutlet weak var btInputLogin: UIButton!
    
    var logInStatus: Bool = false
    var userAccount: UserAccount?
    
//    var userAccount: UserAccount?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
//        print("UserDataSetupVC.swift viewDidLoad")

        // Do any additional setup after loading the
//        self.title = "會員資料設定"
//        navigationItem.title = "會員資料設定"
//        print("userAccount: \(userAccount?.userId)")

        if let userData = UserAccount.readUsersFromFile() {
            print("userData",userData)
            userAccount = userData[0]
            logInStatus = COMM_USER_ID == 0 ? false : true
        } else {
            logInStatus = false
        }
        
        SetUI(uiStatus: logInStatus)
    }
    
    
    func SetUI(uiStatus: Bool) {
        
        // true 登入狀態
        if uiStatus == true {
  
            labPhone.text = "姓名："
            labPhone.textColor = UIColor(red: 0.26, green: 0.26, blue: 0.26, alpha: 1.00)
            tfUserPhone.text = userAccount?.userName
            
            labPassword.text = "生日："
            labPassword.textColor = UIColor(red: 0.26, green: 0.26, blue: 0.26, alpha: 1.00)
            let dataFormatter = DateFormatter()
            dataFormatter.locale = Locale(identifier: "zh_Hant_TW")
            dataFormatter.dateFormat = "YYYY-MM-dd"
            let userBirth = dataFormatter.string(from: (userAccount?.userBirth)!)
            tfPassword.text = userBirth
            tfPassword.isSecureTextEntry = false
            
            btLogin.setTitle("登出", for: UIControl.State.normal)
            btRegister.isHidden = true
            
        
        } else { // false 登出狀態
            
            labPhone.text = "*必填 "
            labPhone.textColor = UIColor(red: 0.90, green: 0.42, blue: 0.41, alpha: 1.00)
            tfUserPhone.text = ""
            
            labPassword.text = "*必填 "
            labPassword.textColor = UIColor(red: 0.90, green: 0.42, blue: 0.41, alpha: 1.00)
            tfPassword.text = ""
            tfPassword.isSecureTextEntry = true
            
            btLogin.setTitle("會員登入", for: UIControl.State.normal)
            btRegister.setTitle("清除", for: UIControl.State.normal)
            btRegister.isHidden = false
            
        }
        
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
 
        if logInStatus == false {
            let userPhone = tfUserPhone.text!
            let userPwd = tfPassword.text!
            
            if userPhone.isEmpty || userPwd.isEmpty {
                let controller = UIAlertController(title: "提示訊息", message: "請輸入帳號、密碼", preferredStyle: .alert)
                   let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                   controller.addAction(okAction)
                   present(controller, animated: true, completion: nil)
                return
                
            }
            
            Login(userPhone: userPhone, userPwd: userPwd) { [self] (userId) in
                print("clickLogin.userId: \(userId ?? 0)")
                if userId == 0 {
                    logInStatus = false
                    print("Login Fail~")
                } else {
                    if let userData = UserAccount.readUsersFromFile() {
                        print("clickLogin.userData",userData)
                        userAccount = userData[0]
                        logInStatus = COMM_USER_ID == 0 ? false : true
                    } else {
                        logInStatus = false
                    }
                    print("Login OK!!!")
                }
                
                print("clickLogin.logInStatus", logInStatus)

                DispatchQueue.main.async {
                   SetUI(uiStatus: logInStatus)
                }
                
            }
            
        } else {  //if logInStatus == false
            LogOut()
            logInStatus = false
            SetUI(uiStatus: logInStatus)
            
        } // if logInStatus == false
        
    }
    
    
    @IBAction func clickRegister(_ sender: Any) {
        tfUserPhone.text = ""
        tfPassword.text = ""
    }
    
    @IBAction func ckickInputLogin(_ sender: Any) {
        tfUserPhone.text = "0900123456"
        tfPassword.text = "P@ssw0rd"
    }
    
}
