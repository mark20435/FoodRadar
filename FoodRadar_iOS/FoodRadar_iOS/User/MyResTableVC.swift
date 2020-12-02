//
//  MyResTableVC.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/9.
//

import UIKit
import Foundation

class MyResTableVC: UITableViewController, UISearchBarDelegate {

    @IBOutlet weak var searchMyRes: UISearchBar!
    
    @IBOutlet var tableViewMyRes: UITableView!
    
    
    let urlMyResServlet = "MyResServlet"
    var myResArray: [MyRes]?
    var myResArrayAll: [MyRes]?
    
//    let cellReuseIdentifier = "cell"  // 表頭高度
//    let cellSpacingHeight: CGFloat = 50  // 表頭高度
//    // 表頭高度
//    // Set the spacing between sections
//    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
//        return cellSpacingHeight
//    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        searchMyRes.delegate = self

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
//        // 表頭高度
//        // These tasks can also be done in IB if you prefer.
//        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
                
        
        
        print("USER_ID(1): \(COMM_USER_ID)")
        
        getMyRes(userId: COMM_USER_ID) { (myResArray) in
            if let myResArray = myResArray {
                self.myResArray = myResArray
                self.myResArrayAll = self.myResArray
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }
        
        // Table隔線只顯示到有資料的部分
        tableView.tableFooterView = UIView()
        
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return myResArray?.count ?? 0
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: "MyResTableVCell", for: indexPath) as! MyResTableVCell
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyResTableVCell", for: indexPath) as! MyResTableVCell

        // Configure the cell...
        let myRes = myResArray?[indexPath.row]
        cell.lbResName.text = myRes?.resName
        cell.lbResHour.text = "營業時間: " + resHour(myRes!.resHours)
        cell.lbResTel.text = "聯絡電話: " + myRes!.resTel
        cell.lbResAddress.text = "餐廳地址: " + myRes!.resAddress
//        var text = myRes?.resHours ?? "" + "\n"
//        text += myRes?.resTel ?? "" + "\n"
//        text += myRes?.resAddress ?? ""
//        cell.lbResAddress.text = text
        cell.imageView?.contentMode = .scaleAspectFill
        let url = NetworkController().baseURL.appendingPathComponent(urlMyResServlet)
        let imageSize = 400
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let getImagePost = MyResGetImageStruct(id: myRes?.resId ?? 0, imageSize: imageSize)
        request.httpBody = try? JSONEncoder().encode(getImagePost)
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            if let data = data {
                DispatchQueue.main.async {
//                    cell.ivMyRes?.image = nil
                    cell.ivMyRes?.image = UIImage(data: data)
//                    self.tableView.reloadData()
                }
            }
//            else {
//                cell.imageView?.image = UIImage(named: "logo_foodradar")
//            }
        }.resume()
        
        
        return cell
    }
    

    
    func getMyRes (userId: Int, complection: @escaping ([MyRes]?) -> Void ) {
        
        print("getMyRes.userId: \(userId)")
        
        let url = NetworkController().baseURL.appendingPathComponent(urlMyResServlet)

        var request = URLRequest(url: url)
        let myResStruct = MyResStruct(id: userId)
        
        request.httpMethod = "POST"
        request.httpBody = try? JSONEncoder().encode(myResStruct)
//        print("request: \(String(data: myResStruct, encodeing: .utf8))")
//        print("data:\(String(data: request!, encoding: .utf8))")
        
        URLSession.shared.dataTask(with: request) {(dataTask, responseTask, errorTask) in
            let decoder = JSONDecoder()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            dateFormatter.calendar = Calendar(identifier: .iso8601)
            decoder.dateDecodingStrategy = .formatted(dateFormatter)
            if let dataFromDb = dataTask,
               let myResArray = try? decoder.decode([MyRes].self, from: dataFromDb) {

//                print("resId: \(myResArray[0].resId)")
                complection (myResArray)
             
            } else {
                complection (nil)
            }
        }.resume()
    }
    
    
    
//    func getImageMyRes(resId: Int,complection: @escaping (UIImage?) -> Void) {
//
//        let url = NetworkController().baseURL.appendingPathComponent(urlMyResServlet)
//        let imageSize = 400
//        var request = URLRequest(url: url)
//        request.httpMethod = "POST"
//
//        let getImagePost = MyResGetImageStruct(id: resId, imageSize: imageSize)
//        request.httpBody = try? JSONEncoder().encode(getImagePost)
//        URLSession.shared.dataTask(with: request) { (data, response, error) in
//            if let data = data,
//               let image = UIImage(data: data) {
//                DispatchQueue.main.async {
//                    self.view.backgroundColor = UIColor(patternImage: image)
//                }
//            }
//        }.resume()
//    }

    
    func resHour (_ hourJson: String) -> String {
        let data = hourJson.data(using: .utf8)!
//        print("hourJson: " + hourJson)
// '{"11": "11:30~14:30","12": "17:00~22:00","21": "11:30~14:30","22": "17:00~22:00","31": "11:30~14:30","32": "17:00~22:00","41": "11:30~14:30","42": "17:00~22:00","51": "11:30~14:30","52": "17:00~22:00","61": "11:30~14:30","62": "17:00~22:00","71": "11:30~14:30","72": "17:00~22:00"}'
        
        if let dic = try? JSONDecoder().decode([String: String].self, from: data) {
            var resHourList: [[String]] = [[String]]()
            
            for (key, value) in dic {
                resHourList.append(contentsOf: [[key],[value]])
            }
    //        dump(resHourList)
            
            let currentDate = Date()
    //        let dataFormatter = DateFormatter()
    //        dataFormatter.locale = Locale(identifier: "zh_Hant_TW")
    //        dataFormatter.dateFormat = "YYYY-MM-dd HH:mm:ss"
    //        let stringDate = dataFormatter.string(from: currentDate)
    //        print(stringDate)

            let calender = Calendar(identifier:Calendar.Identifier.gregorian)
            let comps = (calender as NSCalendar?)?.components(NSCalendar.Unit.weekday, from: currentDate)
            // 獲得當前日期是一個星期的第幾天
            var weekOfDate = 0
//            Calendar星期日是回傳1，所以直接對應到json的7(星期日)，其餘的星期則減1後對應json
            if let compsWeek  = comps?.weekday {
                if compsWeek == 1 { weekOfDate = 7 } else { weekOfDate = compsWeek - 1 }
            }

            let resHourWeekDaySerNo = ["1", "2", "3"]
            let resHour = resHourWeekDaySerNo.reduce("", {combineHour, weekDaySerNo in

                let weekKey = String(weekOfDate) + weekDaySerNo
//                print("weekKey: \(weekKey)")
                var getResHour = ""
                if let i = resHourList.firstIndex(of: [weekKey]) {
//                    print("starts with '[i]' => \(resHourList[i])")
//                    print("starts with '[i][0]' => \(resHourList[i][0])")
//                    print("starts with '[i+1][0]' => \(resHourList[i+1][0])")
//                    print("combineHour: \(combineHour)")
                    getResHour += (combineHour == "" ? "" : "\(combineHour), ") + resHourList[i+1][0]
                } else {
                    getResHour = combineHour
                }
//                print("getResHour: \(getResHour)")
                return getResHour
            })
//            print("resHour: \(resHour)")
            if resHour.isEmpty { return "本日公休" } else { return resHour }
        } else {
            return "無營業時間資訊"
        }
    }
    

    
    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath)
    {
        let verticalPadding: CGFloat = 8
        let horizontalPadding: CGFloat = 8

        let maskLayer = CALayer()
        maskLayer.cornerRadius = 10    //if you want round edges
        maskLayer.backgroundColor = UIColor.black.cgColor
        maskLayer.frame = CGRect(x: cell.bounds.origin.x, y: cell.bounds.origin.y, width: cell.bounds.width, height: cell.bounds.height).insetBy(dx: horizontalPadding, dy: verticalPadding/2)
        cell.layer.mask = maskLayer
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        let text = searchBar.text ?? ""
        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
        if text == "" {
            myResArray = myResArrayAll
        } else {
            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
            myResArray = myResArrayAll!.filter({ (myResArray) -> Bool in
                return myResArray.resName.uppercased().contains(text.uppercased())
            })
        }
        tableView.reloadData()
    }
    
    // 點擊鍵盤上的Search按鈕時將鍵盤隱藏
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
    
    
    
}
