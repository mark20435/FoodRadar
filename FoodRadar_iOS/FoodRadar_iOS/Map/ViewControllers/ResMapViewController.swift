//
//  ResMapViewController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/10.
//

import UIKit
import MapKit
import CoreLocation

class ResMapViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var cvResMap: UICollectionView!
    
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        allRess?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
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
    

    @IBOutlet weak var mapView: MKMapView!
    let manager = CLLocationManager()
    var getLocation = true
    var allRess: [Res]?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        manager.requestWhenInUseAuthorization()
        
        NetworkController.shared.getAllResEnable(userId: 3) { (ress) in
            if let ress = ress {
                self.allRess = ress
                DispatchQueue.main.async {
                    self.cvResMap.reloadData()
                }
                
            }
        }
        
        cvResMap.register(UINib(nibName: "ResMapCollectionViewCell", bundle: nil) , forCellWithReuseIdentifier: "ResMapCollectionViewCell")
        
        //addRes()
    }
    
    
//    func addRes() {
//        let annotation = MKPointAnnotation()
//        annotation.coordinate = CLLocationCoordinate2D(latitude: 25.0528973, longitude: 121.5433009)
//        annotation.title = "萬有全涮羊肉"
//        annotation.subtitle = "台北市中山區南京東路三段223巷8號"
//        mapView.addAnnotation(annotation)
//    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension ResMapViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
        if getLocation {
            getLocation = false
            let region = MKCoordinateRegion(center: userLocation.coordinate, latitudinalMeters: 500, longitudinalMeters: 500)
            
            mapView.setRegion(region, animated: true)
        }
        
        if let allRess = allRess {
            let annotations: [MKAnnotation] = allRess.map {
                let annotation = MKPointAnnotation()
                annotation.coordinate = CLLocationCoordinate2D(latitude: $0.resLat, longitude: $0.resLon)
                annotation.title = $0.resName
                annotation.subtitle = $0.resAddress
                return annotation
            }
            mapView.addAnnotations(annotations)
        }
    }
}
