//
//  ResMaintainViewController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/2.
//

import UIKit

class ResMaintainViewController: UIViewController, UISearchBarDelegate {

    @IBOutlet weak var tableView: UITableView!
    // 儲存所有資料
    var allRess: [Res]?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        NetworkController.shared.getAllRes { (ress) in
            if let ress = ress {
                self.allRess = ress
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
                
            }
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

}

extension ResMaintainViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return allRess?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var res : Res
        res = allRess![indexPath.row]
        let cellId = "resCell"
        let cell = tableView.dequeueReusableCell(withIdentifier: cellId) as! ResCell
        cell.lbResName.text = res.resName
        cell.lbResAddress.text = res.resAddress
        cell.lbResTel.text = res.resTel
        cell.lbResCategoryInfo.text = res.resCategoryInfo
        
        NetworkController.shared.getImage(servletName: "ResServlet", id: res.resId, imageSize: 100) { (image) in
            if let image = image {
                DispatchQueue.main.async {
                    cell.ivRes.image = image
                }
            }
        }
        
        return cell
    }
    
    
}
