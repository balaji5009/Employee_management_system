import React, { useState, useEffect } from 'react'
import axios from 'axios'
import toast from 'react-hot-toast'
import { useAuth } from '../context/AuthContext'
import { User, Calendar, DollarSign, Download, Eye, Clock, CheckCircle, XCircle } from 'lucide-react'
import { format } from 'date-fns'

const EmployeeDashboard = () => {
  const { user } = useAuth()
  const [employeeData, setEmployeeData] = useState(null)
  const [attendanceRecords, setAttendanceRecords] = useState([])
  const [salaryRecords, setSalaryRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState('profile')

  useEffect(() => {
    if (user?.employeeId) {
      fetchEmployeeData()
      fetchAttendanceRecords()
      fetchSalaryRecords()
    }
  }, [user])

  const fetchEmployeeData = async () => {
    try {
      const response = await axios.get(`/api/employees/${user.employeeId}`)
      setEmployeeData(response.data)
    } catch (error) {
      toast.error('Failed to fetch employee data')
    }
  }

  const fetchAttendanceRecords = async () => {
    try {
      const response = await axios.get(`/api/attendance?employeeId=${user.employeeId}`)
      setAttendanceRecords(response.data.slice(0, 10)) // Last 10 records
    } catch (error) {
      toast.error('Failed to fetch attendance records')
    }
  }

  const fetchSalaryRecords = async () => {
    try {
      const response = await axios.get(`/api/salaries?employeeId=${user.employeeId}`)
      setSalaryRecords(response.data.slice(0, 6)) // Last 6 months
    } catch (error) {
      toast.error('Failed to fetch salary records')
    } finally {
      setLoading(false)
    }
  }

  const handleDownloadPayslip = async (month, year) => {
    try {
      const response = await axios.get(`/api/payslip/download/${user.employeeId}/${month}/${year}`, {
        responseType: 'blob'
      })
      
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `Payslip_${month}_${year}.pdf`)
      
      document.body.appendChild(link)
      link.click()
      link.remove()
      window.URL.revokeObjectURL(url)
      
      toast.success('Payslip downloaded successfully')
    } catch (error) {
      toast.error('Failed to download payslip')
    }
  }

  const handleViewPayslip = async (month, year) => {
    try {
      const response = await axios.get(`/api/payslip/view/${user.employeeId}/${month}/${year}`, {
        responseType: 'blob'
      })
      
      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }))
      window.open(url, '_blank')
      
      setTimeout(() => {
        window.URL.revokeObjectURL(url)
      }, 100)
    } catch (error) {
      toast.error('Failed to view payslip')
    }
  }

  const getStatusBadge = (status) => {
    const statusConfig = {
      PRESENT: { color: 'bg-green-100 text-green-800', icon: CheckCircle },
      ABSENT: { color: 'bg-red-100 text-red-800', icon: XCircle },
      LATE: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
      HALF_DAY: { color: 'bg-blue-100 text-blue-800', icon: Clock }
    }
    
    const config = statusConfig[status] || statusConfig.PRESENT
    const Icon = config.icon
    
    return (
      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${config.color}`}>
        <Icon className="h-3 w-3 mr-1" />
        {status.replace('_', ' ')}
      </span>
    )
  }

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount)
  }

  const getMonthName = (month) => {
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ]
    return months[month - 1]
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white shadow rounded-lg p-6">
        <div className="flex items-center space-x-4">
          <div className="h-16 w-16 bg-primary-100 rounded-full flex items-center justify-center">
            <User className="h-8 w-8 text-primary-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">
              Welcome, {employeeData?.firstName} {employeeData?.lastName}
            </h1>
            <p className="text-gray-600">{employeeData?.position} • {employeeData?.department?.name}</p>
            <p className="text-sm text-gray-500">Employee ID: {employeeData?.id}</p>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="bg-white shadow rounded-lg">
        <div className="border-b border-gray-200">
          <nav className="-mb-px flex space-x-8 px-6">
            <button
              onClick={() => setActiveTab('profile')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'profile'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              <User className="h-4 w-4 inline mr-2" />
              Profile
            </button>
            <button
              onClick={() => setActiveTab('attendance')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'attendance'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              <Calendar className="h-4 w-4 inline mr-2" />
              Attendance
            </button>
            <button
              onClick={() => setActiveTab('salary')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'salary'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              <DollarSign className="h-4 w-4 inline mr-2" />
              Salary
            </button>
          </nav>
        </div>

        <div className="p-6">
          {/* Profile Tab */}
          {activeTab === 'profile' && employeeData && (
            <div className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Personal Information</h3>
                  <dl className="space-y-3">
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Full Name</dt>
                      <dd className="text-sm text-gray-900">{employeeData.firstName} {employeeData.lastName}</dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Email</dt>
                      <dd className="text-sm text-gray-900">{employeeData.email}</dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Phone</dt>
                      <dd className="text-sm text-gray-900">{employeeData.phone}</dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Address</dt>
                      <dd className="text-sm text-gray-900">{employeeData.address}</dd>
                    </div>
                  </dl>
                </div>
                <div>
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Employment Details</h3>
                  <dl className="space-y-3">
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Position</dt>
                      <dd className="text-sm text-gray-900">{employeeData.position}</dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Department</dt>
                      <dd className="text-sm text-gray-900">{employeeData.department?.name}</dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Hire Date</dt>
                      <dd className="text-sm text-gray-900">
                        {format(new Date(employeeData.hireDate), 'MMM dd, yyyy')}
                      </dd>
                    </div>
                    <div>
                      <dt className="text-sm font-medium text-gray-500">Status</dt>
                      <dd className="text-sm text-gray-900">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          employeeData.status === 'ACTIVE' 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {employeeData.status}
                        </span>
                      </dd>
                    </div>
                  </dl>
                </div>
              </div>
            </div>
          )}

          {/* Attendance Tab */}
          {activeTab === 'attendance' && (
            <div className="space-y-4">
              <h3 className="text-lg font-medium text-gray-900">Recent Attendance</h3>
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Date
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Status
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Check In
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Check Out
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Notes
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {attendanceRecords.map((record) => (
                      <tr key={record.id}>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {format(new Date(record.date), 'MMM dd, yyyy')}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          {getStatusBadge(record.status)}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {record.checkInTime || '-'}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {record.checkOutTime || '-'}
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-900">
                          {record.notes || '-'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              
              {attendanceRecords.length === 0 && (
                <div className="text-center py-12">
                  <Calendar className="mx-auto h-12 w-12 text-gray-400" />
                  <h3 className="mt-2 text-sm font-medium text-gray-900">No attendance records</h3>
                  <p className="mt-1 text-sm text-gray-500">
                    No attendance records found.
                  </p>
                </div>
              )}
            </div>
          )}

          {/* Salary Tab */}
          {activeTab === 'salary' && (
            <div className="space-y-4">
              <h3 className="text-lg font-medium text-gray-900">Salary History</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {salaryRecords.map((salary) => {
                  const netSalary = salary.basicSalary + salary.allowances - salary.deductions
                  
                  return (
                    <div key={salary.id} className="bg-gray-50 rounded-lg p-4">
                      <div className="flex justify-between items-start mb-3">
                        <div>
                          <h4 className="text-lg font-medium text-gray-900">
                            {getMonthName(salary.month)} {salary.year}
                          </h4>
                        </div>
                        <div className="flex space-x-2">
                          <button
                            onClick={() => handleViewPayslip(salary.month, salary.year)}
                            className="text-blue-600 hover:text-blue-900"
                            title="View Payslip"
                          >
                            <Eye className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleDownloadPayslip(salary.month, salary.year)}
                            className="text-green-600 hover:text-green-900"
                            title="Download Payslip"
                          >
                            <Download className="h-4 w-4" />
                          </button>
                        </div>
                      </div>
                      <dl className="space-y-2">
                        <div className="flex justify-between">
                          <dt className="text-sm text-gray-500">Basic Salary</dt>
                          <dd className="text-sm text-gray-900">{formatCurrency(salary.basicSalary)}</dd>
                        </div>
                        <div className="flex justify-between">
                          <dt className="text-sm text-gray-500">Allowances</dt>
                          <dd className="text-sm text-green-600">+{formatCurrency(salary.allowances)}</dd>
                        </div>
                        <div className="flex justify-between">
                          <dt className="text-sm text-gray-500">Deductions</dt>
                          <dd className="text-sm text-red-600">-{formatCurrency(salary.deductions)}</dd>
                        </div>
                        <div className="flex justify-between border-t pt-2">
                          <dt className="text-sm font-medium text-gray-900">Net Salary</dt>
                          <dd className="text-sm font-medium text-gray-900">{formatCurrency(netSalary)}</dd>
                        </div>
                      </dl>
                    </div>
                  )
                })}
              </div>
              
              {salaryRecords.length === 0 && (
                <div className="text-center py-12">
                  <DollarSign className="mx-auto h-12 w-12 text-gray-400" />
                  <h3 className="mt-2 text-sm font-medium text-gray-900">No salary records</h3>
                  <p className="mt-1 text-sm text-gray-500">
                    No salary records found.
                  </p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default EmployeeDashboard