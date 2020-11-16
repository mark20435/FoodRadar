//
//  ResListCollectionViewController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/13.
//

import UIKit

private let reuseIdentifier = "ResMapCollectionViewCell"

class ResListCollectionViewController: UICollectionViewController {

    var allRess: [Res]?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        NetworkController.shared.getAllResEnable(userId: 3) { (ress) in
            if let ress = ress {
                self.allRess = ress
                DispatchQueue.main.async {
                    self.collectionView.reloadData()
                }
                
            }
        }
        
        self.collectionView!.register(UINib(nibName: "ResMapCollectionViewCell", bundle: nil) , forCellWithReuseIdentifier: reuseIdentifier)

        // Do any additional setup after loading the view.
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

    // MARK: UICollectionViewDataSource



    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of items
        allRess?.count ?? 0
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        var res : Res
        res = allRess![indexPath.row]
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "\(ResMapCollectionViewCell.self)", for: indexPath) as! ResMapCollectionViewCell
        cell.lbResName.text = res.resName
        cell.lbResAddress.text = res.resAddress
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

    // MARK: UICollectionViewDelegate

    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(_ collectionView: UICollectionView, shouldHighlightItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment this method to specify if the specified item should be selected
    override func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
    override func collectionView(_ collectionView: UICollectionView, shouldShowMenuForItemAt indexPath: IndexPath) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, canPerformAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, performAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) {
    
    }
    */

}
