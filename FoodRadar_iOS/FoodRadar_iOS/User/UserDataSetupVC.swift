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
            tfUserPhone.isEnabled = false
            
            labPassword.text = "生日："
            labPassword.textColor = UIColor(red: 0.26, green: 0.26, blue: 0.26, alpha: 1.00)
            let dataFormatter = DateFormatter()
            dataFormatter.locale = Locale(identifier: "zh_Hant_TW")
            dataFormatter.dateFormat = "YYYY-MM-dd"
            let userBirth = dataFormatter.string(from: (userAccount?.userBirth)!)
            tfPassword.text = userBirth
            tfPassword.isSecureTextEntry = false
            tfPassword.isEnabled = false
            
            btLogin.setTitle("登出", for: UIControl.State.normal)
            btRegister.isHidden = true
            
            let url = NetworkController().baseURL.appendingPathComponent("UserAccountServlet")
            let imageSize = 400
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            
            let getImagePost = UserAvatarStruct(imageSize: imageSize, id: COMM_USER_ID)
            request.httpBody = try? JSONEncoder().encode(getImagePost)
            URLSession.shared.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    DispatchQueue.main.async {
                        self.imAvatar.image = UIImage(data: data)
                    }
                }
            }.resume()
            
        
        } else { // false 登出狀態
            
            labPhone.text = "*必填 "
            labPhone.textColor = UIColor(red: 0.90, green: 0.42, blue: 0.41, alpha: 1.00)
            tfUserPhone.text = ""
            tfUserPhone.isEnabled = true
            
            labPassword.text = "*必填 "
            labPassword.textColor = UIColor(red: 0.90, green: 0.42, blue: 0.41, alpha: 1.00)
            tfPassword.text = ""
            tfPassword.isSecureTextEntry = true
            tfPassword.isEnabled = true
            
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
                    DispatchQueue.main.async {
                        self.view.showToast(text: "登入失敗")
                    }
                    print("Login Fail~")
                } else {
                    if let userData = UserAccount.readUsersFromFile() {
                        print("clickLogin.userData",userData)
                        userAccount = userData[0]
                        logInStatus = COMM_USER_ID == 0 ? false : true
                    } else {
                        logInStatus = false
                    }
                    
                    
//                    imAvatar.contentMode = .scaleAspectFill
                    let url = NetworkController().baseURL.appendingPathComponent("UserAccountServlet")
                    let imageSize = 400
                    var request = URLRequest(url: url)
                    request.httpMethod = "POST"
                    
                    let getImagePost = UserAvatarStruct(imageSize: imageSize, id: COMM_USER_ID)
                    request.httpBody = try? JSONEncoder().encode(getImagePost)
                    URLSession.shared.dataTask(with: request) { (data, response, error) in
                        if let data = data {
                            DispatchQueue.main.async {
                                imAvatar.image = UIImage(data: data)
                            }
                        }
                    }.resume()
                    
                    DispatchQueue.main.async {
                        self.view.showToast(text: "登入成功")
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
            self.imAvatar.image = UIImage(systemName: "person.crop.circle")
            
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

extension UIView {

    func showToast(text: String){
        
        self.hideToast()
        let toastLb = UILabel()
        toastLb.numberOfLines = 0
        toastLb.lineBreakMode = .byWordWrapping
        toastLb.backgroundColor = UIColor.black.withAlphaComponent(0.7)
        toastLb.textColor = UIColor.white
        toastLb.layer.cornerRadius = 10.0
        toastLb.textAlignment = .center
        toastLb.font = UIFont.systemFont(ofSize: 15.0)
        toastLb.text = text
        toastLb.layer.masksToBounds = true
        toastLb.tag = 9999//tag：hideToast實用來判斷要remove哪個label
        
        let maxSize = CGSize(width: self.bounds.width - 40, height: self.bounds.height)
        var expectedSize = toastLb.sizeThatFits(maxSize)
        var lbWidth = maxSize.width
        var lbHeight = maxSize.height
        if maxSize.width >= expectedSize.width{
            lbWidth = expectedSize.width
        }
        if maxSize.height >= expectedSize.height{
            lbHeight = expectedSize.height
        }
        expectedSize = CGSize(width: lbWidth, height: lbHeight)
        toastLb.frame = CGRect(x: ((self.bounds.size.width)/2) - ((expectedSize.width + 20)/2), y: self.bounds.height - expectedSize.height - 40 - 20, width: expectedSize.width + 20, height: expectedSize.height + 20)
        self.addSubview(toastLb)
        
        UIView.animate(withDuration: 1.5, delay: 1.5, animations: {
            toastLb.alpha = 0.0
        }) { (complete) in
            toastLb.removeFromSuperview()
        }
    }
    
    func hideToast(){
        for view in self.subviews{
            if view is UILabel , view.tag == 9999{
                view.removeFromSuperview()
            }
        }
    }
    
    /*
     // https://medium.com/@j847676/swift-4-%E8%87%AA%E5%88%B6%E7%B0%A1%E6%98%93toast-%E6%96%87%E5%AD%97%E6%8F%90%E7%A4%BA-a1ced67edcda
     
     class MainVC: UIViewController {
         
         override func viewDidLoad() {
             super.viewDidLoad()
             
             //就一行而已，在任何你想用的地方使用吧
             self.view.showToast(text: "這是一個Toast的範例")
           
             //也可以直接在keyWindow呈現
             UIApplication.shared.keyWindow?.showToast(text: "這是一個Toast的範例")

         }
     }
     */
    
}

