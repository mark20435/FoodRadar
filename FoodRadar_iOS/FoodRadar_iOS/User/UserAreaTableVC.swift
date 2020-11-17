//
//  UserAreaTableVC.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/4.
//

import UIKit

class UserAreaTableVC: UITableViewController {
    
    var userAccount: UserAccount?

    override func viewDidLoad() {
//        print("UserAreaTableVC viewDidLoad")
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        navigationItem.title = "會員專區"
        // Table隔線只顯示到有資料的部分
        tableView.tableFooterView = UIView()
        
//        if let userAccount = UserAccount.readUsersFromFile() {
//            self.userAccount = userAccount[0]
//            COMM_USER_ID = userAccount[0].userId
//        }
//        print("UserAreaTableVC viewDidLoad end")
        
//        if let tabItems = tabBarController?.tabBar.items {
//            // In this case we want to modify the badge number of the third tab:
//            let tabItem = tabItems[4]
//            if COMM_USER_ID == 0 {
//                tabItem.badgeValue = nil
//            } else {
//                tabItem.badgeValue = String(COMM_USER_ID)
//            }
//        }
        
        
    }
    @IBAction func clickContanctUs(_ sender: Any) {
        
        let controller = UIAlertController(title: "聯繫我們", message: "※團隊成員※\n高新緯 tep101_01@tibame.com.tw\n陳暘璿 tep101_05@tibame.com.tw\n王瑞琦 tep101_08@tibame.com.tw\n簡輝峰 tep101_09@tibame.com.tw", preferredStyle: .alert)
        // preferredStyle: .alert  => 顯示在中間
//        let okAction = UIAlertAction(title: "關閉", style: .default) { (_) in
//
//        }
//        controller.addAction(okAction)
        let cancelAction = UIAlertAction(title: "關閉", style: .cancel, handler: nil)
        controller.addAction(cancelAction)
        present(controller, animated: true, completion: nil)
        
    }
    
    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 7
    }

    /*
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation
/*
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
//
//        print("prepare", userAccount, segue.destination, segue.destination.children)
//        if let controller = segue.destination as? UserDataSetupVC {
//            controller.userAccount = userAccount
//        }
//        print("prepare end")

        
    }
 */
    

}
