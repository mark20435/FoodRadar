//
//  ResPickerTableViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/8.
//

import UIKit

class ResPickerTableViewController: UITableViewController {
    
    var resAddressArray = [ResAddress]()
    let url_resAddress = URL(string: common_url + "ResAddressServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "選擇餐廳"
        showResAddress()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    /* 顯示餐廳資訊 **/
    @objc func showResAddress() {
        /* 取得餐廳地址資訊 **/
        var requestParam = [String: Any]()
        requestParam["action"] = "getRes"
        executeTask(url_resAddress!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode([ResAddress].self, from: data!) {
                        self.resAddressArray = result
                        DispatchQueue.main.async {
                            self.tableView.reloadData()
                        }
                    }
                }
            }else {
                print(error!.localizedDescription)  //印出錯誤資訊
            }
        }
    }
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    /* tableView 長度 **/
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return resAddressArray.count
    }
    
    /* 顯示資料 **/
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ResPickerTableViewCell", for: indexPath) as! ResPickerTableViewCell
        
        let resAddress = resAddressArray[indexPath.row]
        
        //顯示在tableViewCell上
        cell.lbResName.text = resAddress.resName
        cell.lbResAddress.text = resAddress.resAddress
        return cell
    }
    
    
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
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}
