import React, { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import axios from 'axios'
import { Users, Building2, Calendar, DollarSign, TrendingUp, Clock } from 'lucide-react'

const Dashboard = () => {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    totalEmployees: 0,
    totalDepartments: 0,
    presentToday: 0,
    pendingSalaries: 0
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      setLoading(true)
      
      // Fetch different data based on user role
      const promises = []
      
      if (user.role === 'ADMIN' || user.role === 'HR') {
        promises.push(
          axios.get('/api/employees/count'),
          axios.get('/api/departments'),
          axios.get(`/api/attendance/present-count/${new Date().toISOString().split('T')[0]}`),
          axios.get('/api/salary')
        )
      } else {
        // For employees, fetch their own data
        promises.push(
          axios.get(`/api/employees/${user.employeeId}`),
          axios.get(`/api/attendance/employee/${user.employeeId}`),
          axios.get(`/api/salary/employee/${user.employeeId}`)
        )
      }

      const responses = await Promise.allSettled(promises)
      
      if (user.role === 'ADMIN' || user.role === 'HR') {
        setStats({
          totalEmployees: responses[0].status === 'fulfilled' ? responses[0].value.data : 0,
          totalDepartments: responses[1].status === 'fulfilled' ? responses[1].value.data.length : 0,
          presentToday: responses[2].status === 'fulfilled' ? responses[2].value.data : 0,
          pendingSalaries: responses[3].status === 'fulfilled' ? responses[3].value.data.length : 0
        })
      }
    } catch (error) {
      console.error('Error fetching dashboard data:', error)
    } finally {
      setLoading(false)
    }
  }

  const StatCard = ({ title, value, icon: Icon, color, description }) => (
    <div className="bg-white overflow-hidden shadow rounded-lg">
      <div className="p-5">
        <div className="flex items-center">
          <div className="flex-shrink-0">
            <Icon className={`h-6 w-6 ${color}`} />
          </div>
          <div className="ml-5 w-0 flex-1">
            <dl>
              <dt className="text-sm font-medium text-gray-500 truncate">{title}</dt>
              <dd>
                <div className="text-lg font-medium text-gray-900">{value}</div>
              </dd>
            </dl>
          </div>
        </div>
        {description && (
          <div className="mt-3">
            <p className="text-sm text-gray-600">{description}</p>
          </div>
        )}
      </div>
    </div>
  )

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Welcome Section */}
      <div className="bg-white shadow rounded-lg p-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">
              Welcome back, {user?.username}!
            </h1>
            <p className="mt-1 text-sm text-gray-600">
              Here's what's happening in your organization today.
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-primary-100 text-primary-800">
              {user?.role}
            </span>
          </div>
        </div>
      </div>

      {/* Stats Grid */}
      {(user.role === 'ADMIN' || user.role === 'HR') && (
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          <StatCard
            title="Total Employees"
            value={stats.totalEmployees}
            icon={Users}
            color="text-blue-600"
            description="Active employees in the system"
          />
          <StatCard
            title="Departments"
            value={stats.totalDepartments}
            icon={Building2}
            color="text-green-600"
            description="Total departments"
          />
          <StatCard
            title="Present Today"
            value={stats.presentToday}
            icon={Calendar}
            color="text-yellow-600"
            description="Employees present today"
          />
          <StatCard
            title="Salary Records"
            value={stats.pendingSalaries}
            icon={DollarSign}
            color="text-purple-600"
            description="Total salary records"
          />
        </div>
      )}

      {/* Quick Actions */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900 mb-4">
            Quick Actions
          </h3>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {(user.role === 'ADMIN' || user.role === 'HR') && (
              <>
                <button className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg border border-gray-300 hover:border-gray-400">
                  <div>
                    <span className="rounded-lg inline-flex p-3 bg-primary-50 text-primary-600 ring-4 ring-white">
                      <Users className="h-6 w-6" />
                    </span>
                  </div>
                  <div className="mt-8">
                    <h3 className="text-lg font-medium">
                      <a href="/employees" className="focus:outline-none">
                        <span className="absolute inset-0" aria-hidden="true" />
                        Manage Employees
                      </a>
                    </h3>
                    <p className="mt-2 text-sm text-gray-500">
                      Add, edit, or remove employee records
                    </p>
                  </div>
                </button>

                <button className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg border border-gray-300 hover:border-gray-400">
                  <div>
                    <span className="rounded-lg inline-flex p-3 bg-green-50 text-green-600 ring-4 ring-white">
                      <Building2 className="h-6 w-6" />
                    </span>
                  </div>
                  <div className="mt-8">
                    <h3 className="text-lg font-medium">
                      <a href="/departments" className="focus:outline-none">
                        <span className="absolute inset-0" aria-hidden="true" />
                        Manage Departments
                      </a>
                    </h3>
                    <p className="mt-2 text-sm text-gray-500">
                      Organize and manage departments
                    </p>
                  </div>
                </button>
              </>
            )}

            <button className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg border border-gray-300 hover:border-gray-400">
              <div>
                <span className="rounded-lg inline-flex p-3 bg-yellow-50 text-yellow-600 ring-4 ring-white">
                  <Calendar className="h-6 w-6" />
                </span>
              </div>
              <div className="mt-8">
                <h3 className="text-lg font-medium">
                  <a href="/attendance" className="focus:outline-none">
                    <span className="absolute inset-0" aria-hidden="true" />
                    {user.role === 'EMPLOYEE' ? 'My Attendance' : 'Manage Attendance'}
                  </a>
                </h3>
                <p className="mt-2 text-sm text-gray-500">
                  {user.role === 'EMPLOYEE' ? 'View your attendance records' : 'Track employee attendance'}
                </p>
              </div>
            </button>

            <button className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg border border-gray-300 hover:border-gray-400">
              <div>
                <span className="rounded-lg inline-flex p-3 bg-purple-50 text-purple-600 ring-4 ring-white">
                  <DollarSign className="h-6 w-6" />
                </span>
              </div>
              <div className="mt-8">
                <h3 className="text-lg font-medium">
                  <a href="/salary" className="focus:outline-none">
                    <span className="absolute inset-0" aria-hidden="true" />
                    {user.role === 'EMPLOYEE' ? 'My Salary' : 'Manage Salaries'}
                  </a>
                </h3>
                <p className="mt-2 text-sm text-gray-500">
                  {user.role === 'EMPLOYEE' ? 'View salary and download payslips' : 'Generate and manage salaries'}
                </p>
              </div>
            </button>
          </div>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900 mb-4">
            System Status
          </h3>
          <div className="space-y-3">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="h-2 w-2 bg-green-400 rounded-full"></div>
              </div>
              <div className="ml-3">
                <p className="text-sm text-gray-600">System is running smoothly</p>
              </div>
            </div>
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="h-2 w-2 bg-green-400 rounded-full"></div>
              </div>
              <div className="ml-3">
                <p className="text-sm text-gray-600">Database connection is stable</p>
              </div>
            </div>
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="h-2 w-2 bg-green-400 rounded-full"></div>
              </div>
              <div className="ml-3">
                <p className="text-sm text-gray-600">All services are operational</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard