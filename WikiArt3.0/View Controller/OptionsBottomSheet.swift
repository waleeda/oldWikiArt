import UIKit

protocol OptionsBottomSheetDelegate: AnyObject {
    func optionsBottomSheet(_ sheet: OptionsBottomSheet, didSelect category: PaintingCategory?, layout: Layout)
}

class OptionsBottomSheet: UIViewController, UITableViewDataSource, UITableViewDelegate {
    private let categories: [PaintingCategory]
    private var selectedCategory: PaintingCategory?
    private var layout: Layout
    weak var delegate: OptionsBottomSheetDelegate?

    private var tableView: UITableView!
    private var segmentedControl: UISegmentedControl!

    init(categories: [PaintingCategory], selectedCategory: PaintingCategory?, layout: Layout) {
        self.categories = categories
        self.selectedCategory = selectedCategory
        self.layout = layout
        super.init(nibName: nil, bundle: nil)
        modalPresentationStyle = .pageSheet
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemBackground

        tableView = UITableView(frame: .zero, style: .plain)
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "Cell")
        tableView.dataSource = self
        tableView.delegate = self
        view.addSubview(tableView)

        segmentedControl = UISegmentedControl(items: ["List", "Grid", "Sheet"])
        segmentedControl.translatesAutoresizingMaskIntoConstraints = false
        segmentedControl.selectedSegmentIndex = layout.rawValue
        view.addSubview(segmentedControl)

        let applyButton = UIButton(type: .system)
        applyButton.translatesAutoresizingMaskIntoConstraints = false
        applyButton.setTitle(NSLocalizedString("OK", comment: ""), for: .normal)
        applyButton.addTarget(self, action: #selector(applyTapped), for: .touchUpInside)
        view.addSubview(applyButton)

        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),

            segmentedControl.topAnchor.constraint(equalTo: tableView.bottomAnchor, constant: 12),
            segmentedControl.centerXAnchor.constraint(equalTo: view.centerXAnchor),

            applyButton.topAnchor.constraint(equalTo: segmentedControl.bottomAnchor, constant: 12),
            applyButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -12),
            applyButton.centerXAnchor.constraint(equalTo: view.centerXAnchor)
        ])
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return categories.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        let cat = categories[indexPath.row]
        cell.textLabel?.text = cat.title
        cell.accessoryType = (cat == selectedCategory) ? .checkmark : .none
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedCategory = categories[indexPath.row]
        tableView.reloadData()
    }

    @objc private func applyTapped() {
        let selectedLayout = Layout(rawValue: segmentedControl.selectedSegmentIndex) ?? layout
        delegate?.optionsBottomSheet(self, didSelect: selectedCategory, layout: selectedLayout)
        dismiss(animated: true, completion: nil)
    }
}
